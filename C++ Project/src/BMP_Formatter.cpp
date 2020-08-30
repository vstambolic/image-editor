#include "BMP_Formatter.h"

#include "Image.h"
#include "exceptions.h"

BMP_Formatter::BMP_Formatter(const std::string& _file_path) : Formatter(_file_path) {
	bmp_header = std::make_unique<BMP_Header>();
	dib_header = std::make_unique<DIB_Header>();
}

Pixel BMP_Formatter::get_pixel(std::ifstream & ifs) const {
	Dword dword;
	if (this->dib_header->get_bits_per_pixel() == _32_BIT) {
		ifs.read(reinterpret_cast<char*>(&dword), 4);		// ARGB
		dword = rol(dword, CHAR_BIT);						// RGBA
	}
	else {
		ifs.read(reinterpret_cast<char*>(&dword), 3);		// 0RGB
		dword <<= CHAR_BIT;									// RGB0
		dword |= 0xFFU;										// RGBA
	}
	return Pixel(dword);
}

void BMP_Formatter::put_pixel(std::ofstream & ofs, const Pixel& pixel) const {	// rotating: <bit> since C++20 ?														
	Dword dword = static_cast<Dword>(pixel);			// RGBA	
	dword = ror(dword, CHAR_BIT);						// ARGB
	ofs.write(reinterpret_cast<char*>(&dword), sizeof(Pixel));				// puts : BGRA 
}


void BMP_Formatter::read(Image& image) {

	std::ifstream ifs(this->file_path, std::ios::in | std::ios::binary);
	if (!ifs.is_open())
		throw File_Opening_Exception(file_path);

	ifs >> *bmp_header;					// read BMP header
	ifs >> *dib_header;					// read DIB header

	const unsigned bpp = dib_header->get_bits_per_pixel();
	if (bpp != _24_BIT && bpp != _32_BIT)
		throw BMP_Format_Not_Supported(bpp);

	const unsigned m = dib_header->get_height();
	const unsigned n = dib_header->get_width();
	Layer layer(file_path, m, n);
	
	const unsigned leftover = n * bpp / 8 % 4;
	const unsigned padding = (leftover ? 4 - leftover : 0);
	
	ifs.seekg(bmp_header->get_bitmap_data_offset());

	for (auto it = layer.get_matrix().rbegin(); it != layer.get_matrix().rend(); ++it) { 
		for (auto& jt : *it)
			jt = get_pixel(ifs);
		ifs.seekg(padding, std::ios::cur);				// Padding
	}

	ifs.close();

	image.add_layer(layer);
}

void BMP_Formatter::write(const Image& image) const {
	std::ofstream ofs(this->file_path, std::ios::out | std::ios::binary | std::ios::trunc);
	if (!ofs.is_open())
		throw File_Opening_Exception(file_path);
	
	const Layer& layer = image.get_project();

	bmp_header->set_file_size(layer.data_size());
	dib_header->set_width(layer.get_width());
	dib_header->set_height(layer.get_height());
	dib_header->set_bitmap_size(layer.data_size());
	
	ofs << *bmp_header;
	ofs << *dib_header;
	
	for (auto it = layer.get_matrix().rbegin(); it != layer.get_matrix().rend(); ++it)
		for (auto jt : *it)
			put_pixel(ofs, jt);

	ofs.close();
}



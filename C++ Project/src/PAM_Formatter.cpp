#include "PAM_Formatter.h"

#include "Image.h"
#include "Utilities.h"

#include <fstream>
#include "exceptions.h"

Pixel PAM_Formatter::get_pixel(std::ifstream & ifs) const {
	Dword dword;
	if (depth == 4) {										// RGBA 
		ifs.read(reinterpret_cast<char*>(&dword), 4);		// ABGR
		dword =	swap_bytes(dword);							// RGBA
	}
	else {													// RGB
		ifs.read(reinterpret_cast<char*>(&dword), 3);		// 0BGR
		dword = swap_bytes(dword);							// RGB0
		dword |= 0xFFU;										// RGBA
	}
	return Pixel(dword);
}
void PAM_Formatter::put_pixel(std::ofstream & ofs, const Pixel & pixel) const {
	Dword dword = static_cast<Dword>(pixel);					// RGBA	
	dword = swap_bytes(dword);									// ABGR
	ofs.write(reinterpret_cast<char*>(&dword), sizeof(Pixel));  // puts : RGBA 
}
void PAM_Formatter::read(Image& image) {
	std::ifstream ifs(this->file_path, std::ios::in | std::ios::binary);
	if (!ifs.is_open())
		throw File_Opening_Exception(file_path);

	std::string dummy;
	std::getline(ifs, dummy, ' ');			// P7\nWIDTH_
	ifs >> width;
	std::getline(ifs, dummy, ' ');			// \nHEIGHT_
	ifs >> height;
	std::getline(ifs, dummy,' ');			// \nDEPTH
	ifs >> depth;
	if (depth < 3)
		throw PAM_Format_Not_Supported(depth);			// YET!!
	std::getline(ifs, dummy);				// \n
	std::getline(ifs, dummy);				// MAXVAL
	std::getline(ifs, dummy);				// TUPLTYPE
	std::getline(ifs, dummy);				// ENDHDR

	Layer layer(file_path, height, width);
	for (auto& it : layer.get_matrix())
		for (auto& jt : it)
			jt = get_pixel(ifs);
	ifs.close();

	image.add_layer(layer);
}
void PAM_Formatter::write(const Image& image) const {
	std::ofstream ofs(this->file_path, std::ios::out | std::ios::binary | std::ios::trunc);
	if (!ofs.is_open())
		throw File_Opening_Exception(file_path);

	const Layer& layer = image.get_project();

	ofs << "P7\n";
	ofs << "WIDTH " << layer.get_width() << '\n';
	ofs << "HEIGHT " << layer.get_height() << '\n';
	ofs << "DEPTH " << depth << '\n';
	ofs << "MAXVAL 255\n";
	ofs << "TUPLTYPE RGB_ALPHA\n";
	ofs << "ENDHDR\n";

	for (auto it : layer.get_matrix())
		for (auto jt : it)
			put_pixel(ofs, jt);

	ofs.close();
}

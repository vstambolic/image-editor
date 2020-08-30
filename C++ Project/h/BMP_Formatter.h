#pragma once

#include "formatter.h"
#include "Utilities.h" 
#include <memory>

#define _24_BIT 24
#define _32_BIT 32

class Pixel;

class BMP_Formatter : public Formatter {
private:

	class BMP_Header;
	class DIB_Header;

	std::unique_ptr<BMP_Header> bmp_header;
	std::unique_ptr<DIB_Header> dib_header;

	Pixel get_pixel(std::ifstream& ifs) const override;
	void put_pixel(std::ofstream& ofs, const Pixel& pixel) const override;
public:

	BMP_Formatter(const std::string& _file_path);

	void read(Image& image);
	void write(const Image& image) const;

	// Header friends
	friend std::ifstream& operator>> (std::ifstream& ifs, BMP_Header& bmp_header);
	friend std::ofstream& operator<< (std::ofstream& ofs, const BMP_Header& bmp_header);

	friend std::ifstream& operator>> (std::ifstream& ifs, DIB_Header& dib_header);
	friend std::ofstream& operator<< (std::ofstream& ofs, const DIB_Header& bmp_header);

};

// BMP Header -----------------------------------------------------------------------
class BMP_Formatter::BMP_Header {		
private:
	static const Word id;
	Dword file_size = 0x36;	
	static const Word vacant_1;
	static const Word vacant_2;
	Dword bitmap_data_offset = 0x36;
public:
	BMP_Header() {}

	friend std::ifstream& operator>> (std::ifstream& ifs, BMP_Header& bmp_header);
	friend std::ofstream& operator<< (std::ofstream& ofs, const BMP_Header& bmp_header);

	Dword get_bitmap_data_offset() const;
	Dword get_file_size() const;

	void set_file_size(const Dword& _file_size);
};



// DIB Header -----------------------------------------------------------------------
class BMP_Formatter::DIB_Header {		
private:
	static const Dword dib_header_size;
	Dword width;
	Dword height;
	static const Word color_planes;
	Word bits_per_pixel = _32_BIT;
	static const Dword bi_rgb;
	Dword bitmap_size;
	static const Dword print_resolution_1;
	static const Dword print_resolution_2;
	static const Dword number_of_colors;
	static const Dword important_colors;
public:
// Input / Output -----------------------------------

	friend std::ifstream& operator>>(std::ifstream& ifs, DIB_Header& dib_header);
	friend std::ofstream& operator<<(std::ofstream& ofs, const DIB_Header& dib_header);

// Getters / Setters --------------------------------
	Dword get_width() const {
		return width;
	}
	Dword get_height() const {
		return height;
	}
	Word get_bits_per_pixel() const {
		return bits_per_pixel;
	}

	void set_width(const Dword& _width) {
		this->width = _width;
	}
	void set_height(const Dword& _height) {
		this->height = _height;
	}
	void set_bitmap_size(const Dword& size) {
		this->bitmap_size = size;
	}
};
#include "BMP_Formatter.h"


const Dword BMP_Formatter::DIB_Header::dib_header_size = 0x00'00'00'28;
const Word BMP_Formatter::DIB_Header::color_planes = 0x00'01;
const Dword BMP_Formatter::DIB_Header::bi_rgb = 0x00'00'00'00;
const Dword BMP_Formatter::DIB_Header::print_resolution_1 = 0x00'00'0B'13;
const Dword BMP_Formatter::DIB_Header::print_resolution_2 = 0x00'00'0B'13;
const Dword BMP_Formatter::DIB_Header::number_of_colors = 0x00'00'00'00;
const Dword BMP_Formatter::DIB_Header::important_colors = 0x00'00'00'00;


std::ifstream& operator>> (std::ifstream& ifs, BMP_Formatter::DIB_Header& dib_header) {
	ifs.seekg(0x12);
	ifs.read(reinterpret_cast<char*>(&dib_header.width), sizeof(Dword));
	ifs.read(reinterpret_cast<char*>(&dib_header.height), sizeof(Dword));

	ifs.seekg(0x1C);
	ifs.read(reinterpret_cast<char*>(&dib_header.bits_per_pixel), sizeof(Dword));

	ifs.seekg(0x22);
	ifs.read(reinterpret_cast<char*>(&dib_header.bitmap_size), sizeof(Dword));
	return ifs;
}

std::ofstream & operator<<(std::ofstream & ofs, const BMP_Formatter::DIB_Header & dib_header){
	
	ofs.write(reinterpret_cast<const char*>(&dib_header.dib_header_size), sizeof(Dword));
	ofs.write(reinterpret_cast<const char*>(&dib_header.width), sizeof(Dword));
	ofs.write(reinterpret_cast<const char*>(&dib_header.height), sizeof(Dword));
	ofs.write(reinterpret_cast<const char*>(&dib_header.color_planes), sizeof(Word));
	ofs.write(reinterpret_cast<const char*>(&dib_header.bits_per_pixel), sizeof(Word));
	ofs.write(reinterpret_cast<const char*>(&dib_header.bi_rgb), sizeof(Dword));
	ofs.write(reinterpret_cast<const char*>(&dib_header.bitmap_size), sizeof(Dword));
	ofs.write(reinterpret_cast<const char*>(&dib_header.print_resolution_1), sizeof(Dword));
	ofs.write(reinterpret_cast<const char*>(&dib_header.print_resolution_2), sizeof(Dword));
	ofs.write(reinterpret_cast<const char*>(&dib_header.number_of_colors), sizeof(Dword));
	ofs.write(reinterpret_cast<const char*>(&dib_header.important_colors), sizeof(Dword));
	
	return ofs;
}
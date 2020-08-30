#include "bmp_formatter.h"
#include "Utilities.h"
#include <fstream>

const Word BMP_Formatter::BMP_Header::id = 0x4D42; // Little endian: 0x424D;
const Word BMP_Formatter::BMP_Header::vacant_1 = 0x0000;
const Word BMP_Formatter::BMP_Header::vacant_2 = 0x0000;

Dword BMP_Formatter::BMP_Header::get_bitmap_data_offset() const {
	//return swap_bytes(this->bitmap_data_offset);
	return this->bitmap_data_offset;
}

Dword BMP_Formatter::BMP_Header::get_file_size() const {
	return this->file_size;
}

void BMP_Formatter::BMP_Header::set_file_size(const Dword & _file_size) {
	this->file_size += _file_size;
}

std::ifstream& operator>> (std::ifstream& ifs, BMP_Formatter::BMP_Header& bmp_header) {
	ifs.seekg(0x2);
	ifs.read(reinterpret_cast<char*>(&bmp_header.file_size), sizeof(Dword));
	ifs.seekg(0xA);
	ifs.read(reinterpret_cast<char*>(&bmp_header.bitmap_data_offset), sizeof(Dword));
	return ifs;
}

std::ofstream& operator<< (std::ofstream& ofs, const BMP_Formatter::BMP_Header& bmp_header) { // little endian: low byte transfered first
	//Word tmp_word = swap_bytes(bmp_header.id);	// 0x424D -> 0x4D42
	ofs.write(reinterpret_cast<const char*>(&bmp_header.id), sizeof(Word));
	ofs.write(reinterpret_cast<const char*>(&bmp_header.file_size), sizeof(Dword));
	ofs.write(reinterpret_cast<const char*>(&bmp_header.vacant_1), sizeof(Word));
	ofs.write(reinterpret_cast<const char*>(&bmp_header.vacant_2), sizeof(Word));
	ofs.write(reinterpret_cast<const char*>(&bmp_header.bitmap_data_offset), sizeof(Dword));
	//ofs << bmp_header.id << bmp_header.file_size << bmp_header.vacant_1 << bmp_header.vacant_2 << bmp_header.bitmap_data_offset;
	return ofs;
}

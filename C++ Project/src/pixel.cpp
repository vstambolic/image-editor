#include "pixel.h"
#include <iomanip>

std::ostream & operator<<(std::ostream & os, const Pixel & pixel) {
	os << std::setfill('0') << std::setw(8) << std::hex << std::uppercase << (Dword)pixel;
	return os;
}



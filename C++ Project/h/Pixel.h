#pragma once

#include "Utilities.h"

#include <ostream>
#include <string>

#define MAX_BYTE 0xFFU

// Double Pixel ----------------------------------------------------------

class D_Pixel {
public:
	double red, green, blue, alpha;
	D_Pixel(double _red = 0., double _green = 0., double _blue = 0., double _alpha = 0.) : red(_red), green(_green), blue(_blue), alpha(_alpha) {}
};

// Regular Pixel ----------------------------------------------------------
class Pixel {
public:
	Pixel(Byte _red = 0, Byte _green = 0, Byte _blue = 0, Byte _alpha = 0) : red(_red), green(_green), blue(_blue), alpha(_alpha) {}

	// Conversion to Dword --------------------------------------------------
	explicit Pixel(Dword dword) {
		red = (dword >> 24) & 0xFF;
		green = (dword >> 16) & 0xFF;
		blue = (dword >> 8) & 0xFF;
		alpha =  dword  & 0xFF;
	}
	explicit operator Dword() const {
		Dword tmp = 0U;
		tmp |= red; tmp <<= CHAR_BIT;
		tmp |= green; tmp <<= CHAR_BIT;
		tmp |= blue; tmp <<= CHAR_BIT;
		tmp |= alpha;
		return tmp;
	}
	
	// Output ---------------------------------------------------------------
	friend std::ostream& operator<< (std::ostream& os, const Pixel& pixel);

	// Conversiong to D_Pixel -----------------------------------------------
	operator D_Pixel() const {
		return D_Pixel(red, green, blue, alpha);
	}

	Pixel(const D_Pixel& d_pixel) {
		auto double_to_byte = [](double d_component) -> Byte {
			Byte byte; // unsigned byte;
			if (d_component >= MAX_BYTE)
				byte = MAX_BYTE;
			else
				if (d_component <= 0)
					byte = 0;
				else 
					byte = (Byte)d_component;
			return byte;
		};

		red = double_to_byte(d_pixel.red);
		green = double_to_byte(d_pixel.green);
		blue = double_to_byte(d_pixel.blue);
		alpha = double_to_byte(d_pixel.alpha);
	}

	// Values ---------------------------------------------------------------
	Byte red, green, blue, alpha;

	double scale_alpha() const {
		return alpha / 255.;
	}

};


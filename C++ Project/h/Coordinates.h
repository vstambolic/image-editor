#pragma once
#include <iostream>

struct Coordinates {
	long x = 0;		// height offset from upper left corner
	long y = 0;		// width offset from upper left corner

	bool operator==(const Coordinates& other) const {
		return (this->x == other.x) && (this->y == other.y);
	}
	friend std::istream& operator >> (std::istream& is, Coordinates& coordinates) {
		is >> coordinates.x >> coordinates.y;
		return is;
	}
	friend std::ostream& operator << (std::ostream& os, const Coordinates& coordinates) {
		os << coordinates.x << ' ' << coordinates.y;
		return os;
	}
};

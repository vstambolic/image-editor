#pragma once
#include "Coordinates.h"
#include <ostream>

#include <boost/property_tree/ptree.hpp>
using boost::property_tree::ptree;


class Rectangle {
private:
	Coordinates coordinates;
	unsigned long width;
	unsigned long height;

public:
	Rectangle(Coordinates _coordinates, unsigned long _width, unsigned long _height) : coordinates(_coordinates), width(_width), height(_height) {	}

	// Comparison --------------------------------------------------------------------

	bool operator==(const Rectangle& other) const {
		return (this->coordinates == other.coordinates) && (this->width == other.width) && (this->height == other.height);
	}
	bool operator<(const Rectangle& other) const {
		return this->area() < other.area();
	}
	bool operator>(const Rectangle& other) const {
		return this->area() > other.area();
	}
	unsigned long area() const {
		return width * height;
	}


	bool contains(const Coordinates& point) const {
		return (point.x >= coordinates.x && point.x < coordinates.x + (long)height && point.y >= coordinates.y && point.y < coordinates.y + (long)width);
	}
	bool contains(const Rectangle& other) const {
		return other.coordinates.x >= this->coordinates.x && other.coordinates.y >= this->coordinates.y &&
			  (other.coordinates.x + other.height) <= (this->coordinates.x + this->height) && (other.coordinates.y + other.width) <= (this->coordinates.y + this->width);
	}

	// Getter ------------------------------------------------------------------------

	Coordinates get_coordinates() const {
		return coordinates;
	}
	unsigned long get_width() const {
		return width;
	}
	unsigned long get_height() const {
		return height;
	}

	// Output ------------------------------------------------------------------------

	friend std::ostream & operator<<(std::ostream & os, const Rectangle & rectangle); 
	ptree to_ptree() const;
};
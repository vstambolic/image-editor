#pragma once
#include <string>
#include <ostream>

#include "Coordinates.h"
#include "Pixel_Matrix.h"

#include <boost/property_tree/ptree.hpp>
using boost::property_tree::ptree;

#define MAX_OPACITY 100
#define EMPTY_LAYER ""

class Layer  {
public:

	// Constructor ------------------------------------------------------
	Layer(const std::string& _file_path = "",
		  unsigned long _height = 0,
		  unsigned long _width = 0,
		  bool _active = true,
		  bool _visible = true,
		  unsigned _opacity = 0,
		  const Coordinates& _coordinates = { 0,0 });


	// Getters -----------------------------------------------------------
	std::string get_source_file() const {
		return source_file;
	}
	unsigned long get_width() const {
		return width;
	}
	unsigned long get_height() const {
		return height;
	}
	unsigned get_opacity() const {
		return opacity;
	}
	bool get_visible_status() const {
		return visible;
	}
	bool get_active_status() const {
		return active;
	}
	unsigned long data_size() const {
		return width * height * sizeof(Pixel);
	}
	Pixel_Matrix& get_matrix() {
		return matrix;
	}
	const Pixel_Matrix& get_matrix() const{
		return matrix;
	}
	const Coordinates& get_coordinates() const {
		return this->coordinates;
	}


	// Setters -----------------------------------------------------------
	void set_opacity(int opacity);
	void set_visible(bool new_status) {
		this->visible = new_status;
	}
	void set_active(bool new_status) {
		this->active = new_status;
	}
	void set_position(const Coordinates& _coordinates) {
		this->coordinates = _coordinates;
	}
	

	// Output ------------------------------------------------------------
	friend std::ostream& operator<<(std::ostream& os, const Layer& layer);
	ptree to_ptree() const;

	// Cut ---------------------------------------------------------------
	void cut(unsigned long x_axis_begin, unsigned long x_axis_end, unsigned long y_axis_begin, unsigned long y_axis_end);


private:
	std::string source_file;
	unsigned long width;
	unsigned long height;
	Pixel_Matrix matrix;
	bool active;
	bool visible;
	unsigned opacity;
	Coordinates coordinates;

	friend class Image;
};


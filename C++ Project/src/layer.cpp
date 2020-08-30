#include "layer.h"

#include <vector>


// Constructor ----------------------------------------------------------------
Layer::Layer(const std::string& _file_path, 
			unsigned long _height, 
			unsigned long _width, 
			bool _active,
			bool _visible,
			unsigned _opacity, 
			const Coordinates& _coordinates) :  source_file(_file_path),
												height(_height),
												width(_width),
												matrix(height, std::vector<Pixel>(width)),
												active(_active),
												visible(_visible),
												opacity(_opacity),
												coordinates(_coordinates) {}

// Cut ---------------------------------------------------------------
void Layer::cut(unsigned long x_axis_begin, unsigned long x_axis_end, unsigned long y_axis_begin, unsigned long y_axis_end) {
	matrix.erase(matrix.begin() + y_axis_begin, matrix.begin() + y_axis_end);		// cut rows

	if (x_axis_end - x_axis_begin)	// If there's anything to cut
		for (auto& vec : matrix)													// cut columns
			vec.erase(vec.begin() + x_axis_begin, vec.begin() + x_axis_end);
	
	this->width -= x_axis_end - x_axis_begin;
	this->height -= y_axis_end - y_axis_begin;
}

// Output ---------------------------------------------------------------

std::ostream& operator<<(std::ostream& os, const Layer& layer) {
	os << '\t' << "Source file: " << layer.source_file << std::endl;
	os << '\t' << "Dimensions: " << layer.width << " x " << layer.height << std::endl;
	os << '\t' << "Coordinates within image: (" << layer.coordinates.x << ", " << layer.coordinates.y << ")" << std::endl;
	os << '\t' << "Opacity: " << layer.opacity << std::endl;
	os << '\t' << "Visible: " << std::boolalpha << layer.visible << std::endl;
	os << '\t' << "Active: " << std::boolalpha << layer.active << std::endl;
	return os;
}

ptree Layer::to_ptree() const {
	ptree pt;
	pt.put("source_file", source_file);
	pt.put("width", width);
	pt.put("height", height);
	pt.put("coordinates", coordinates);
	pt.put("opacity", opacity);
	pt.put("visible", visible);
	pt.put("active", active);
	return pt;
}

// Opacity ---------------------------------------------------------------
void Layer::set_opacity(int _opacity) {
	if (_opacity < 0)
		opacity = 0;
	else
		opacity = _opacity;

	for (auto& vec : matrix)
		std::for_each(vec.begin(), vec.end(), [this](Pixel& pixel) { pixel.alpha = (Byte)std::min(255., (double)opacity / MAX_OPACITY * pixel.alpha); });
}

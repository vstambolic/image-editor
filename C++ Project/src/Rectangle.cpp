#include "Rectangle.h"


std::ostream & operator<<(std::ostream & os, const Rectangle & rectangle) {
	os << "\tUpper left corner : (" << rectangle.coordinates.x << ", " << rectangle.coordinates.y << ")" << std::endl;
	os << "\tDimensions: " << rectangle.width << " x " << rectangle.height << std::endl;
	return os;
}

ptree Rectangle::to_ptree() const {
	ptree pt;
	pt.put("coordinates", coordinates);
	pt.put("width", width);
	pt.put("height", height);
	return pt;
}

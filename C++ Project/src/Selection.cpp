#include "Selection.h"

#include <algorithm>
#include <boost/property_tree/ptree.hpp>

#include "exceptions.h"

// Operations ------------------------------------------------------------------

void Selection::add_rectangle(const Rectangle & new_rectangle) {

	auto rectangle = rectangles.begin();
	while (rectangle != rectangles.end()) {

		if (rectangle->contains(new_rectangle))
			throw Selection_Already_Contains_Rectangle();

		if (new_rectangle.contains(*rectangle)) {
			auto old = rectangle++;
			rectangles.erase(old);
		}
		else
			++rectangle;
	}
	rectangles.insert(new_rectangle);
}

bool Selection::is_selected(const Coordinates & point) const {
	for (auto& rectangle : rectangles)
		if (rectangle.contains(point))
			return true;

	return false;
}


// Output ---------------------------------------------------------------------

std::ostream & operator<<(std::ostream & os, const Selection & selection) {
	int i = 1;
	for (auto& rectangle : selection.rectangles)
		os << "Rectangle #" << i++ << std::endl << rectangle;
	os << std::endl;
	return os;

}
ptree Selection::to_ptree() const {
	ptree pt;
	for (auto& rec : rectangles)
		pt.add_child("rectangle", rec.to_ptree());
	return pt;
}

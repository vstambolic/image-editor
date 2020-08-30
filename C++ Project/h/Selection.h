#pragma once

#include <set>
#include <unordered_map>
#include <ostream>

#include "Rectangle.h"

#define ACTIVE true
#define INACTIVE false

using boost::property_tree::ptree;
typedef std::multiset<Rectangle, std::greater<>> Rectangle_Set;	// Priority queue?

class Selection  {
private:
	Rectangle_Set rectangles;		// Order rectangles by their area descending -> greater the chance it contains point

public:
	
	// Operations ------------------------------------------------------------------

	void add_rectangle(const Rectangle& rectangle);
	bool is_selected(const Coordinates& point) const;
	
	// Output ---------------------------------------------------------------------

	friend std::ostream& operator<<(std::ostream & os, const Selection & selection);
	ptree to_ptree() const;
	
};

typedef std::unordered_map<std::string, Selection> Selection_Map;

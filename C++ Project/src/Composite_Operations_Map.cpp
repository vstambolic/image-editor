#include "Composite_Operations_Map.h"

std::ostream& Composite_Operations_Map::details(std::ostream& os) const {
	for (auto it = this->begin(); it != this->end(); ++it)
		os << *(it->second) << std::endl;
	return os;
}
#include "Composite_Operation.h"

#include <boost/property_tree/ptree.hpp>

// Apply ----------------------------------------------

D_Pixel_Matrix Composite_Operation::apply(const Selection_Map & selections, D_Pixel_Matrix & matrix, const Coordinates & coordinates) const {
	for (auto& operation : operations)
		operation->apply(selections, matrix, coordinates);
	return matrix;
}
D_Pixel_Matrix Composite_Operation::apply(D_Pixel_Matrix & matrix) const {
	for (auto& operation : operations)
		operation->apply(matrix);
	return matrix;
}



// Print -----------------------------------------------
std::ostream& operator<<(std::ostream& os, const Composite_Operation& c_op) {
	os << "[" << c_op.name << "] { ";
	for (auto& op : c_op.operations)
		os << op->label() << "; ";
	os << "}" << std::endl;
	return os;
}

// Ptree -----------------------------------------------
ptree Composite_Operation::to_ptree() const {
	ptree pt;
	ptree& node = pt.add("composite_operation", "");
	node.put("<xmlattr>.name", name);
	for (auto& op : operations)
		node.add("operation", op->label());
	return pt;
}

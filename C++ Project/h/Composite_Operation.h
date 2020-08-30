#pragma once

#include "Operation.h"
#include <vector>
#include <memory>
#include <ostream>

#include "XML_Formatter.h"
using boost::property_tree::ptree;


class Composite_Operation : public Operation {
private:
	std::vector<std::shared_ptr<Operation>> operations;
	std::string name;
public:
	// Constructors ----------------------------------------------
	Composite_Operation(const std::string& _name) : name(_name) {}
	Composite_Operation() = default;

	// Getter / setter ----------------------------------------------
	void add_operation(const std::shared_ptr<Operation>& operation) {
		operations.push_back(operation);
	}
	
	void set_name(const std::string& _name) {
		this->name = _name;
	}
	std::string get_name() const {
		return this->name;
	}
	// Iterator -----------------------------------------------------
	std::vector<std::shared_ptr<Operation>>::iterator begin() {
		return this->operations.begin();
	}
	std::vector<std::shared_ptr<Operation>>::iterator end() {
		return this->operations.end();
	}


	// Apply ----------------------------------------------
	D_Pixel_Matrix apply(const Selection_Map& selections, D_Pixel_Matrix& matrix, const Coordinates& coordinates) const;
	D_Pixel_Matrix apply(D_Pixel_Matrix& matrix) const;

	// Output ---------------------------------------------
	friend std::ostream& operator<<(std::ostream& os, const Composite_Operation& c_op);

	// Export / Import ------------------------------------

	ptree to_ptree() const;

	void export_to_file(const std::string& path) const {
		XML_Formatter(path + name + ".fun").write(*this);
	}
	void import_from_file(const std::string& path) {
		XML_Formatter(path).read(*this);
	}

	// Other ----------------------------------------------

	size_t size() const {
		return operations.size();
	}
	std::string label() const override { return name; }
};


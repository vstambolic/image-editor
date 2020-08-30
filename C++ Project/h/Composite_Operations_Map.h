#pragma once
#include <unordered_map>
#include <memory>
#include <string>
#include <ostream>
#include "Composite_Operation.h"


class Composite_Operations_Map : public std::unordered_map<std::string, std::shared_ptr<Composite_Operation>> {
public:
	std::ostream& details(std::ostream& os) const;
};


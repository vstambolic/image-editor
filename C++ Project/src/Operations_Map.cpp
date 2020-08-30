#include "Operations_Map.h"

#include <sstream>

#include "Basic_Operations.h"
#include "Predefined_Operations.h"
#include "Other_Operations.h"

#include "exceptions.h"

const std::unordered_map <std::string, std::function<std::shared_ptr<Operation>(void)>>  Operation_Map::_0_arg_operation_map = {
	{"add",create_instance_0_arg<Add_Operation>},
	{"sub",create_instance_0_arg<Sub_Operation>},
	{"invsub",create_instance_0_arg<Inverse_Sub_Operation>},
	{"mul",create_instance_0_arg<Mul_Operation>},
	{"div",create_instance_0_arg<Div_Operation>},
	{"invdiv",create_instance_0_arg<Inverse_Div_Operation>},
	{"pow",create_instance_0_arg<Pow_Operation>},
	{"max",create_instance_0_arg<Max_Operation>},
	{"min",create_instance_0_arg<Min_Operation>},
	{"log",create_instance_0_arg<Log_Operation>},
	{"abs",create_instance_0_arg<Abs_Operation>},
	{"inv",create_instance_0_arg<Invert_Operation>},
	{"bnw",create_instance_0_arg<BnW_Operation>},
	{"grs",create_instance_0_arg<Grayscale_Operation>},
	{"med",create_instance_0_arg<Median_Operation>},
	{"paint",create_instance_0_arg<Paint_Operation>},
};
const std::unordered_map <std::string, std::function<std::shared_ptr<Operation>(double)>>  Operation_Map::_1_arg_operation_map = {
	{"add",create_instance_1_arg<Add_Operation>},
	{"sub",create_instance_1_arg<Sub_Operation>},
	{"invsub",create_instance_1_arg<Inverse_Sub_Operation>},
	{"mul",create_instance_1_arg<Mul_Operation>},
	{"div",create_instance_1_arg<Div_Operation>},
	{"invdiv",create_instance_1_arg<Inverse_Div_Operation>},
	{"pow",create_instance_1_arg<Pow_Operation>},
	{"max",create_instance_1_arg<Max_Operation>},
	{"min",create_instance_1_arg<Min_Operation>},
};
const std::unordered_map <std::string, std::function<std::shared_ptr<Operation>(double, double, double)>>  Operation_Map::_3_arg_operation_map = {
	{"paint",create_instance_3_args<Paint_Operation>}
};

std::shared_ptr<Operation> Operation_Map::read_operation_code(const std::string& input) {
	std::istringstream iss(input);

	std::string code;
	std::vector<double> args; args.reserve(3);
	iss >> code;

	double dummy;
	for (int i = 0; i<3 && iss >> dummy; ++i)				
		args.push_back(dummy);

	if (_0_arg_operation_map.find(code) != _0_arg_operation_map.end()) {
		if (args.size() == 3 && _3_arg_operation_map.find(code) != _3_arg_operation_map.end())
			return _3_arg_operation_map.at(code)(args[0], args[1], args[2]);
		else
			if (args.size() >= 1 && _1_arg_operation_map.find(code) != _1_arg_operation_map.end())
				return _1_arg_operation_map.at(code)(args[0]);
			else
				return _0_arg_operation_map.at(code)();
	}
	else
		throw Invalid_Operation_Code(input);

}

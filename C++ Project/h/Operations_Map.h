#pragma once

#include "Operation.h"
#include <unordered_map>
#include <string>
#include <memory>
#include <functional>

class Operation_Map {
private:
	template<typename T> static std::shared_ptr<Operation> create_instance_0_arg() { return std::make_shared<T>(); }
	template<typename T> static std::shared_ptr<Operation> create_instance_1_arg(double arg) { return std::make_shared<T>(arg); }
	template<typename T> static std::shared_ptr<Operation> create_instance_3_args(double arg1, double arg2, double arg3) { return std::make_shared<T>(arg1, arg2, arg3); }
	
public:
	static const std::unordered_map < std::string, std::function<std::shared_ptr<Operation>()>> _0_arg_operation_map;
	static const std::unordered_map < std::string, std::function<std::shared_ptr<Operation>(double)>> _1_arg_operation_map;
	static const std::unordered_map < std::string, std::function<std::shared_ptr<Operation>(double, double, double)>> _3_arg_operation_map;

	static std::shared_ptr<Operation> read_operation_code(const std::string& input);
};
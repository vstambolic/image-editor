#pragma once
#include "Operation.h"
#include <cmath>
#include <string>


class Basic_Operation : public Operation {
public:
	Basic_Operation(double _arg) : arg(_arg) {}
	
	D_Pixel_Matrix apply(const Selection_Map& selections, D_Pixel_Matrix& matrix, const Coordinates& coordinates) const override {
		return wrapper(selections, matrix, coordinates, [this](D_Pixel& d_pixel) { this->basic_apply(d_pixel); });
	}
	D_Pixel_Matrix apply(D_Pixel_Matrix& matrix) const override {
		return wrapper(matrix, [this](D_Pixel& d_pixel) { this->basic_apply(d_pixel); });
	}

protected:
	virtual void basic_apply(D_Pixel& d_pixel) const = 0;
	const double arg;
};


class Add_Operation : public Basic_Operation {
public:
	Add_Operation(double _arg = MAX_BYTE/4) : Basic_Operation(_arg) {}
	std::string label() const override { return "add " + std::to_string(arg); }
private:
	void basic_apply(D_Pixel& d_pixel) const override {
		auto add = [this](double& d_component) { d_component += arg; };
		add(d_pixel.red), add(d_pixel.green), add(d_pixel.blue);
	}
};

class Sub_Operation : public Basic_Operation {
public:
	Sub_Operation(double _arg = MAX_BYTE / 4) : Basic_Operation(_arg) {}
	std::string label() const override { return "sub " + std::to_string(arg); }
private:
	void basic_apply(D_Pixel& d_pixel) const override {
		auto sub = [this](double& d_component) { d_component -= arg; };
		sub(d_pixel.red), sub(d_pixel.green), sub(d_pixel.blue);
	}
};

class Inverse_Sub_Operation : public Basic_Operation {
public:
	Inverse_Sub_Operation(double _arg = MAX_BYTE / 2) : Basic_Operation(_arg) {}
	std::string label() const override { return "invsub " + std::to_string(arg); }
private:
	void basic_apply(D_Pixel& d_pixel) const override {
		auto inv_sub = [this](double& d_component) { d_component = arg - d_component; };
		inv_sub(d_pixel.red), inv_sub(d_pixel.green), inv_sub(d_pixel.blue);
	}
};


class Mul_Operation : public Basic_Operation {
public:
	Mul_Operation(double _arg = 2) : Basic_Operation(_arg) {}
	std::string label() const override { return "mul " + std::to_string(arg); }
private:
	void basic_apply(D_Pixel& d_pixel) const override {
		auto mul = [this](double& d_component) { d_component *= arg; };
		mul(d_pixel.red), mul(d_pixel.green), mul(d_pixel.blue);
	}
};


class Div_Operation : public Basic_Operation {
public:
	Div_Operation(double _arg = 2) : Basic_Operation(_arg) {}
	std::string label() const override { return "div " + std::to_string(arg); }
private:
	void basic_apply(D_Pixel& d_pixel) const override {
		auto div = [this](double& d_component) { if (arg) d_component /= arg; };
		div(d_pixel.red), div(d_pixel.green), div(d_pixel.blue);
	}
};


class Inverse_Div_Operation : public Basic_Operation {
public:
	Inverse_Div_Operation(double _arg = MAX_BYTE) : Basic_Operation(_arg) {}
	std::string label() const override { return "invdiv " + std::to_string(arg); }
private:
	void basic_apply(D_Pixel& d_pixel) const override {
		auto inv_div = [this](double& d_component) { if (d_component) d_component = arg / d_component; };
		inv_div(d_pixel.red), inv_div(d_pixel.green), inv_div(d_pixel.blue);
	}
};


class Pow_Operation : public Basic_Operation {
public:
	Pow_Operation(double _arg = 1.3) : Basic_Operation(_arg) {}
	std::string label() const override { return "pow " + std::to_string(arg); }
private:
	void basic_apply(D_Pixel& d_pixel) const override {
		auto pow = [this](double& d_component) { d_component = std::pow(d_component, arg); };
		pow(d_pixel.red), pow(d_pixel.green), pow(d_pixel.blue);
	}
};


class Max_Operation : public Basic_Operation {
public:
	std::string label() const override { return "max " + std::to_string(arg); }
	Max_Operation(double _arg = MAX_BYTE / 2) : Basic_Operation(_arg) {}
private:
	void basic_apply(D_Pixel& d_pixel) const override {
		auto max = [this](double& d_component) { d_component = std::max(d_component, arg); };
		max(d_pixel.red), max(d_pixel.green), max(d_pixel.blue);
	}
};


class Min_Operation : public Basic_Operation {
public:
	std::string label() const override { return "min " + std::to_string(arg); }
	Min_Operation(double _arg = MAX_BYTE / 2) : Basic_Operation(_arg) {}
private:
	void basic_apply(D_Pixel& d_pixel) const override {
		auto min = [this](double& d_component) { d_component = std::min(d_component, arg); };
		min(d_pixel.red), min(d_pixel.green), min(d_pixel.blue);
	}
};



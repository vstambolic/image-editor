#pragma once

#include <exception>
#include <string>

// Formatting --------------------------------------------------------------------------------------------------

class File_Opening_Exception : public std::exception {
private:
	 const std::string message;
public:
	File_Opening_Exception(const std::string& file_path) : message("Error opening file: " + file_path) {}
	const char* what() const override {
		return message.c_str();
	}

};

class BMP_Format_Not_Supported : public std::exception {
private:
	std::string message;
public:
	BMP_Format_Not_Supported(const unsigned& bits_per_pixel)  {
		message = "BMP file with bits_per_pixel parameter " + std::to_string(bits_per_pixel) + " not supported.";
	}
	const char* what() const override {
		return message.c_str();
	}

};

class PAM_Format_Not_Supported : public std::exception {
private:
	std::string message;
public:
	PAM_Format_Not_Supported(const unsigned& depth) {
		message = "PAM file with depth parameter" + std::to_string(depth) + " not supported.";
	}
	const char* what() const override {
		return message.c_str();
	}

};

class Format_Not_Supported : public std::exception {
public:
	const char* what() const override {
		return "File format not supported.";
	}
};

// Image ------------------------------------------------------
class Invalid_Index : public std::exception {
private:
	std::string message;
public:
	Invalid_Index(const int& k) {
		message = "Index " + std::to_string(k) + " out of bounds.";
	}
	const char* what() const override {
		return message.c_str();
	}

};

// Selection ------------------------------------------------------
class Selection_Already_Exists : public std::exception {
private:
	const std::string message;
public:
	Selection_Already_Exists(const std::string& selection_name) : message("Selection \"" + selection_name + "\" already exists.") {}
	const char* what() const override {
		return message.c_str();
	}

};
class Selection_Does_Not_Exist : public std::exception {
private:
	const std::string message;
public:
	Selection_Does_Not_Exist(const std::string& selection_name) : message("Selection \"" + selection_name + "\" does not exist.") {}
	const char* what() const override {
		return message.c_str();
	}

};

class Selection_Already_Contains_Rectangle : public std::exception {
public:
	const char* what() const override {
		return "Rectangle already contained inside selection.";
	}

};


class Rectangle_Out_Of_Range : public std::exception {
private:
	std::string message;
public:
	Rectangle_Out_Of_Range(unsigned long width, unsigned long height) {
		message = "Rectangle out of range " + std::to_string(width) + "x" + std::to_string(height)  + " [width x height].";
	}
	const char* what() const override {
		return message.c_str();
	}

};




// Operation ------------------------------------------------------
class Invalid_Operation_Code : public std::exception {
private:
	const std::string message;
public:
	Invalid_Operation_Code(const std::string& invalid_code) : message("\"" + invalid_code + "\" is invalid operation code.") {}
	const char* what() const override {
		return message.c_str();
	}
};

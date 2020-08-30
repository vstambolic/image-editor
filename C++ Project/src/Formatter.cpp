#include "Formatter.h"

#include <memory>
#include <regex>

#include "exceptions.h"
#include "BMP_Formatter.h"
#include "PAM_Formatter.h"
#include "XML_Formatter.h"

std::unique_ptr<Formatter> Formatter::match_formatter(const std::string & file_path) {
	if (std::regex_match(file_path, std::regex(".*\\.bmp$")))
		return std::make_unique<BMP_Formatter>(file_path);

	if (std::regex_match(file_path, std::regex(".*\\.pam$")))
		return std::make_unique<PAM_Formatter>(file_path);

	if (std::regex_match(file_path, std::regex(".*\\.xml$")))
		return std::make_unique<XML_Formatter>(file_path);

	throw Format_Not_Supported();
}

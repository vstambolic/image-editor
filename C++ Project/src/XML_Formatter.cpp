#include "XML_Formatter.h"

#include <fstream>
#include <regex>
#include <boost/property_tree/xml_parser.hpp>
#include <boost/property_tree/ptree.hpp>

#include "Image.h"
#include "Composite_Operation.h"
#include "Operations_Map.h"



// Helper methods ------------------------------------------------------------

void XML_Formatter::put_pixel(std::ofstream & ofs, const Pixel& pixel) const {	
	Dword dword = (Dword)pixel;
	ofs.write(reinterpret_cast<char*>(&dword), 4);	// ABGR											
}
Pixel XML_Formatter::get_pixel(std::ifstream& ifs) const {
	Dword dword;
	ifs.read(reinterpret_cast<char*>(&dword), 4);		// RGBA
	return Pixel(dword);
}

void XML_Formatter::write(const Image& image) const{
	std::ofstream ofs(this->file_path, std::ios::out | std::ios::trunc);
	if (!ofs.is_open())
		throw File_Opening_Exception(file_path);

	boost::property_tree::xml_writer_settings<std::string> settings('\t', 1);
	write_xml(ofs, image.to_ptree(), settings);
	ofs.close();

	std::string layer_prefix(file_path.begin(), file_path.end() - 4);
	for (unsigned i = 0; i < image.layers_num(); ++i) {
		ofs.open(layer_prefix + "_layer_#" + std::to_string(i) + ".lyr", std::ios::out | std::ios::binary | std::ios::trunc);
		if (!ofs.is_open())
			throw File_Opening_Exception(file_path);

		for (auto& vec : image[i].get_matrix())
			for (auto& pix : vec) 
				put_pixel(ofs, pix);
			
		ofs.close();
	}


}

void XML_Formatter::write(const Composite_Operation& comp_op) const {
	std::ofstream ofs(this->file_path, std::ios::out | std::ios::trunc);
	if (!ofs.is_open())
		throw File_Opening_Exception(file_path);

	boost::property_tree::xml_writer_settings<std::string> settings('\t', 1);
	write_xml(ofs, comp_op.to_ptree(), settings);
	ofs.close();
}


void XML_Formatter::read(Composite_Operation & comp_op) const {
	std::ifstream ifs(this->file_path, std::ios::in);
	if (!ifs.is_open())
		throw File_Opening_Exception(file_path);

	std::string tmp;
	std::getline(ifs, tmp);	// xml header

	std::smatch match;

	std::getline(ifs, tmp);	
	std::regex_match(tmp, match, std::regex("<composite_operation name=\\\"(.*)\\\">"));	// name
	comp_op.set_name(match[1]);

	std::getline(ifs, tmp);	// operation
	while (std::regex_match(tmp, match, std::regex(".*<operation>(.*)</operation>"))) {
		comp_op.add_operation(Operation_Map::read_operation_code(match[1]));
		std::getline(ifs, tmp);
	}


	ifs.close();
}


	
	


void XML_Formatter::read(Image& image) {
	std::ifstream ifs(this->file_path, std::ios::in);
	if (!ifs.is_open())
		throw File_Opening_Exception(file_path);

	std::string tmp;
	std::getline(ifs, tmp);	// xml header


	std::smatch match;

	std::getline(ifs, tmp); // name
	if (image.get_name() == DEFAULT_NAME) {
		std::regex_match(tmp, match, std::regex("<image name=\\\"(.*)\\\">"));
		image.set_name(match[1]);
	}


	std::getline(ifs, tmp); // layers
	if (!std::regex_match(tmp,match,std::regex(".*<layers/>"))) {

		std::getline(ifs, tmp); // layer
		do {
			std::getline(ifs, tmp); // source_file
			std::regex_match(tmp, match, std::regex(".*<source_file>(.*)</source_file>"));
			const std::string source_file = match[1];

			std::getline(ifs, tmp); // width
			std::regex_match(tmp, match, std::regex(".*<width>(.*)</width>"));
			unsigned long width = std::stoul(match[1]);

			std::getline(ifs, tmp); // height
			std::regex_match(tmp, match, std::regex(".*<height>(.*)</height>"));
			unsigned long height = std::stoul(match[1]);

			std::getline(ifs, tmp); // coordinates
			std::regex_match(tmp, match, std::regex(".*<coordinates>(.*)[ ](.*)</coordinates>"));
			const Coordinates coordinates = { std::stol(match[1]),std::stol(match[2]) };

			std::getline(ifs, tmp); // opacity
			std::regex_match(tmp, match, std::regex(".*<opacity>(.*)</opacity>"));
			unsigned opacity = (unsigned)std::stoi(match[1]);


			std::getline(ifs, tmp); // visible
			std::regex_match(tmp, match, std::regex(".*<visible>(.*)</visible>"));
			bool visible = (match[1] == "true" ? true : false);

			std::getline(ifs, tmp); // active
			std::regex_match(tmp, match, std::regex(".*<active>(.*)</active>"));
			bool active = (match[1] == "true" ? true : false);

			Layer layer(source_file, height, width, active, visible, opacity, coordinates);

			std::getline(ifs, tmp);	// <data_file>
			std::regex_match(tmp, match, std::regex(".*<data_file>(.*)</data_file>"));
			std::string data_file = match[1];

			// Layer data
			std::string directory(file_path.begin(), file_path.begin() + file_path.find_last_of('\\') + 1);
			std::ifstream ifs_data_file(directory + data_file, std::ios::in | std::ios::binary);
			if (!ifs_data_file.is_open())
				throw File_Opening_Exception(directory + data_file);

			for (auto& vec : layer.get_matrix())
				for (auto& pix : vec)
					pix = get_pixel(ifs_data_file);
			ifs_data_file.close();
				
			std::getline(ifs, tmp); // </layer>

			image.add_layer(layer);

			std::getline(ifs, tmp); // next
		} while (!std::regex_match(tmp, match, std::regex(".*</layers>")));
	}


	// Active selections ------------------------------

	std::getline(ifs, tmp);
	if (!std::regex_match(tmp, match, std::regex(".*<active_selections/>"))) {
		std::getline(ifs, tmp); // selection name
		do {
			std::regex_match(tmp, match, std::regex(".*<selection name=\\\"(.*)\\\">"));
			std::string name = match[1];


			std::getline(ifs, tmp);	// <rectangle>
			do {
				std::getline(ifs, tmp); // coordinates
				std::regex_match(tmp, match, std::regex(".*<coordinates>(.*)[ ](.*)</coordinates>"));
				const Coordinates coordinates = { std::stol(match[1]),std::stol(match[2]) };

				std::getline(ifs, tmp); // width
				std::regex_match(tmp, match, std::regex(".*<width>(.*)</width>"));
				unsigned long width = std::stoul(match[1]);

				std::getline(ifs, tmp); // height
				std::regex_match(tmp, match, std::regex(".*<height>(.*)</height>"));
				unsigned long height = std::stoul(match[1]);

				image.add_rectangle(name, Rectangle(coordinates, width, height));	// ???SV?V?V?VDFVS??SFVd

				std::getline(ifs, tmp);	// </rectangle> 
				std::getline(ifs, tmp);	//  <rectangle> | </selection>

			} while (!std::regex_match(tmp, match, std::regex(".*</selection>")));

			image.set_selection_status(name, ACTIVE);

			std::getline(ifs, tmp); // <selection>  | </active_selections>

		} while (!std::regex_match(tmp, match, std::regex(".*</active_selections>")));

	}


	// Inactive selections ------------------------------
	std::getline(ifs, tmp);
	if (!std::regex_match(tmp, match, std::regex(".*<inactive_selections/>"))) {
		std::getline(ifs, tmp); // selection name
		do {
			std::regex_match(tmp, match, std::regex(".*<selection name=\\\"(.*)\\\">"));
			std::string name = match[1];


			std::getline(ifs, tmp);	// <rectangle>
			do {
				std::getline(ifs, tmp); // coordinates
				std::regex_match(tmp, match, std::regex(".*<coordinates>(.*)[ ](.*)</coordinates>"));
				const Coordinates coordinates = { std::stol(match[1]),std::stol(match[2]) };

				std::getline(ifs, tmp); // width
				std::regex_match(tmp, match, std::regex(".*<width>(.*)</width>"));
				unsigned long width = std::stoul(match[1]);

				std::getline(ifs, tmp); // height
				std::regex_match(tmp, match, std::regex(".*<height>(.*)</height>"));
				unsigned long height = std::stoul(match[1]);

				image.add_rectangle(name, Rectangle(coordinates, width, height));	// ???SV?V?V?VDFVS??SFVd

				std::getline(ifs, tmp);	// </rectangle> 
				std::getline(ifs, tmp);	//  <rectangle> | </selection>

			} while (!std::regex_match(tmp, match, std::regex(".*</selection>")));

			image.set_selection_status(name, INACTIVE);

			std::getline(ifs, tmp); // <selection>  | </active_selections>

		} while (!std::regex_match(tmp, match, std::regex(".*</inactive_selections>")));

	}

	//image.details(std::cout);
	ifs.close();

}
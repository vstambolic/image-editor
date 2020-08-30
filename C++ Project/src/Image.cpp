#include "image.h"

#include <algorithm>
#include <cmath>
#include <iostream>
#include <regex>
#include <typeinfo>
#include <boost/property_tree/ptree.hpp>

#include "exceptions.h"

#include "BMP_Formatter.h"
#include "BMP_Formatter.h"
#include "XML_Formatter.h"


// Details -------------------------------------------------------------------------------
std::ostream& Image::details(std::ostream& os) const {
	using std::endl;
	os << endl;
	os << "Image: " << name << endl;
	os << "Dimensions: " << project.width << " x " << project.height << std::endl;
	layer_details(os);
	selection_details(os);
	os << endl;
	return os;
}

std::ostream& Image::layer_details(std::ostream& os) const {
	using std::cout;
	using std::endl;
	os << "Number of layers: " << layers.size() << endl;
	for (unsigned i = 0; i < layers.size(); ++i) {
		os << "Layer #" << i << endl;
		os << layers[i];
	}
	return os;
}

std::ostream&  Image::selection_details(std::ostream& os) const {
	os << std::endl << "Active selections (" << active_selections.size() << ")" << std::endl;
	for (auto& selection : active_selections) {
		os << "Selection [" << selection.first << "]" << std::endl;
		os << selection.second << std::endl;
	}

	os << std::endl << "Inactive selections (" << inactive_selections.size() << ")" << std::endl;
	for (auto& selection : inactive_selections) {
		os << "Selection [" << selection.first << "]" << std::endl;
		os << selection.second << std::endl;
	}

	os << std::endl;
	return os;
}



// Layers ------------------------------------------------------------------

void Image::add_layer(const Layer& layer) {
	if (layer.height > project.height) {
		project.height = layer.height;
		project.matrix.resize(project.height);
	}
	if (layer.width > project.width) {
		project.width = layer.width;
		for (auto& row : project.matrix)
			row.resize(project.width);
	}
	layers.push_back(layer);
}

void Image::move_layer_depth(int layer_id, int new_layer_depth) {
	if (layer_id < 0 || layer_id >= (int)layers.size())
		throw Invalid_Index(layer_id);
	if (new_layer_depth < 0 || new_layer_depth >= (int)layers.size())
		throw Invalid_Index(new_layer_depth);

	Layer tmp = layers[layer_id];
	layers.erase(layers.begin() + layer_id);
	layers.insert(layers.begin() + new_layer_depth, tmp);

	this->saved_changes = false;
}

void Image::move_layer_position(unsigned layer_id, Coordinates coordinates) {
	if (coordinates.x < 0)
		coordinates.x = 0;
	else
		if (coordinates.x + layers[layer_id].height >= project.height)
			coordinates.x = project.height - layers[layer_id].height;
	if (coordinates.y < 0)
		coordinates.y = 0;
	else
		if (coordinates.y + layers[layer_id].width >= project.width)
			coordinates.y = project.width - layers[layer_id].width;

	layers[layer_id].set_position(coordinates);

	this->saved_changes = false;
}

void Image::move_layer_and_cut(unsigned layer_id, Coordinates coordinates) {

	if (std::abs(coordinates.x) >= (long)this->get_height() || std::abs(coordinates.y) >= (long)this->get_width()) {
		layers.erase(layers.begin() + layer_id);
		return;
	}
	unsigned long x_axis_begin = 0ul, x_axis_end = 0ul, y_axis_begin = 0ul, y_axis_end = 0ul;

	if (coordinates.x < 0) {
		x_axis_begin = 0ul;
		x_axis_end = -coordinates.x;	// abs(x)
		coordinates.x = 0;

	}
	else
		if (coordinates.x + layers[layer_id].height > project.height) {
			x_axis_begin = project.height - coordinates.x;
			x_axis_end = layers[layer_id].height;
		}
	if (coordinates.y < 0) {
		y_axis_begin = 0ul;
		y_axis_end = -coordinates.y;
		coordinates.y = 0;
	}
	else
		if (coordinates.y + layers[layer_id].width > project.width) {
			y_axis_begin = project.width - coordinates.y;
			y_axis_end = layers[layer_id].width;
		}

	layers[layer_id].cut(x_axis_begin, x_axis_end, y_axis_begin, y_axis_end);
	layers[layer_id].set_position(coordinates);

	this->saved_changes = false;
}

void Image::remove_layer(unsigned layer_id) {
	if (layer_id < 0 || layer_id >= layers.size())
		throw Invalid_Index(layer_id);
	layers.erase(layers.begin() + layer_id);

	this->saved_changes = false;
}

unsigned Image::layers_num() const {
	return layers.size();
}

Layer& Image::operator[](int k) {
	if (k < 0 || k >(int)layers.size() - 1)
		throw Invalid_Index(k);
	else
		return layers[k];
}

const Layer& Image::operator[](int k) const {
	return (const_cast<Image&>(*this))[k];
}

// Files ------------------------------------------------------------------

void Image::load(const std::string& file_path) {
	std::unique_ptr<Formatter> formatter = Formatter::match_formatter(file_path);
	formatter->read(*this);
}

void Image::save(const std::string& file_path) {
	std::unique_ptr<Formatter> formatter = Formatter::match_formatter(file_path);
	if (typeid(*formatter) != typeid(XML_Formatter)) 
		create();
	formatter->write(*this);

	this->saved_changes = true;
}

void Image::create() {

	/*for (auto layer : layers) {
		if (layer.visible) {
			auto row_offset = layer.coordinates.y;
			auto col_offset = layer.coordinates.x;

			for (unsigned i = 0; i < layer.height; i++)
				for (unsigned j = 0; j < layer.width; j++) {
					Pixel upper_pix = layer.matrix[i][j];
					Pixel& bottom_pix = this->project.matrix[row_offset + i][col_offset + j];

					double A_bottom = bottom_pix.scale_alpha();
					double A_upper = upper_pix.scale_alpha();
					double A_result = A_upper + (1 - A_upper)*A_bottom;


					auto set_color = [A_bottom, A_upper, A_result](Byte& bottom_color,const Byte& upper_color) {
						bottom_color = static_cast<Byte>( upper_color * A_upper / A_result + bottom_color * (1 - A_upper)*A_bottom / A_result );
					};

					set_color(bottom_pix.red, upper_pix.red);
					set_color(bottom_pix.blue, upper_pix.blue);
					set_color(bottom_pix.green, upper_pix.green);
					bottom_pix.alpha = (Byte)(255U * A_result);

				}

		}
	}*/
	for (auto& vec : project.matrix)
		std::fill(vec.begin(), vec.end(), Pixel());

	for (std::vector<Layer>::reverse_iterator layer = layers.rbegin(); layer != layers.rend(); layer++) {
		if (layer->visible) {
			auto row_offset = layer->coordinates.x;
			auto col_offset = layer->coordinates.y;

			for (unsigned i = 0; i < layer->height; i++)
				for (unsigned j = 0; j < layer->width; j++) {
					Pixel& upper_pix = this->project.matrix[row_offset + i][col_offset + j]; 
					double A_upper = upper_pix.scale_alpha();

					if (A_upper != 1.0) {

						const Pixel& bottom_pix = layer->matrix[i][j];
						double A_bottom = bottom_pix.scale_alpha();
						double A_result = A_upper + (1 - A_upper)*A_bottom;

						auto set_color = [A_bottom, A_upper, A_result](Byte& upper_color, const Byte& bottom_color) {
							upper_color = static_cast<Byte>(upper_color * A_upper / A_result + bottom_color * (1.0 - A_upper)*A_bottom / A_result);
						};

						set_color(upper_pix.red, bottom_pix.red);
						set_color(upper_pix.blue, bottom_pix.blue);
						set_color(upper_pix.green, bottom_pix.green);
						upper_pix.alpha = (Byte)(MAX_BYTE * A_result);
					}

				}

		}
	}

	
}

ptree Image::to_ptree() const {
	ptree pt;
	ptree& node = pt.add("image", "");
	node.put("<xmlattr>.name", name);

	node.add("layers", "");
	for (size_t i = 0; i < layers.size(); ++i) {
		node.add_child("layers.layer", layers[i].to_ptree()).put("data_file", this->name + "_layer_#" + std::to_string(i) + ".lyr");
	}


	node.add("active_selections", "");
	/* Java Project Compability
	for (auto& sel : active_selections)
		ptree tmp = node.add_child("active_selections.selection", sel.second.to_ptree()).put("<xmlattr>.name", sel.first);
	*/

	node.add("inactive_selections", "");
	/* Java Project Compability

	for (auto& sel : inactive_selections)
		node.add_child("inactive_selections.selection", sel.second.to_ptree()).put("<xmlattr>.name", sel.first);
		*/
	return pt;
}

// Selections ------------------------------------------------------------------

void Image::add_rectangle(const std::string & selection_name, const Rectangle & rectangle) {
	//if (!this->is_within(rectangle))
	//	throw Rectangle_Out_Of_Range(this->get_width(), this->get_height());

	if (selection_exists_as_inactive(selection_name))
		inactive_selections[selection_name].add_rectangle(rectangle);
	else
		active_selections[selection_name].add_rectangle(rectangle);
}

void Image::remove_selection(const std::string & selection_name) {
	if (active_selections.erase(selection_name) || inactive_selections.erase(selection_name))	// returns number of erased elements
		return;
	else
		throw Selection_Does_Not_Exist(selection_name);
}

void Image::set_selection_status(const std::string & selection_name, bool status) {
	if (selection_exists_as_active(selection_name) && status == INACTIVE) {
		inactive_selections[selection_name] = std::move(active_selections[selection_name]);
		active_selections.erase(selection_name);
	}
	else
		if (selection_exists_as_inactive(selection_name) && status == ACTIVE) {
			active_selections[selection_name] = std::move(inactive_selections[selection_name]);
			inactive_selections.erase(selection_name);
		}
		else
			if (!selection_exists(selection_name))
				throw Selection_Does_Not_Exist(selection_name);
}

// Operations --------------------------------------------------------------

void Image::apply(const std::shared_ptr<Operation>& operation) {
	if (active_selections.size()) {
		for (auto& layer : layers)
			if (layer.active) {
				D_Pixel_Matrix operand = (D_Pixel_Matrix)layer.matrix;
				layer.matrix = operation->apply(active_selections, operand, layer.coordinates);
			}
	}
	else
		for (auto& layer : layers)
			if (layer.active) {
				D_Pixel_Matrix operand = (D_Pixel_Matrix)layer.matrix;
				layer.matrix = operation->apply(operand);
			}

	this->saved_changes = false;
}



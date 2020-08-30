#pragma once

#include <vector>
#include <memory>
#include <string>
#include <unordered_map>

#include "Selection.h"
#include "Layer.h"
#include "Coordinates.h"
#include "Operation.h"
#include "exceptions.h"

using boost::property_tree::ptree;

#define DEFAULT_NAME "untitled"


class Image {
public:
	Image(const std::string& _name = DEFAULT_NAME) : name(_name), saved_changes(false) {}

	// Details ------------------------------------------------------------------

	std::ostream& details(std::ostream&) const;
	std::ostream& layer_details(std::ostream&) const;
	std::ostream& selection_details(std::ostream&) const;

	// Layers ------------------------------------------------------------------

	void add_layer(const Layer& layer);
	unsigned layers_num() const;
	void move_layer_depth(int layer_depth, int new_layer_depth);
	void move_layer_position(unsigned layer_id, Coordinates coordinates);
	void move_layer_and_cut(unsigned layer_id, Coordinates coordinates);
	void remove_layer(unsigned layer_id);

	// Files ------------------------------------------------------------------
	void load(const std::string& file_path);
	void save(const std::string& path);
	ptree to_ptree() const;
	void create();

	// Selections ------------------------------------------------------------------
	void add_rectangle(const std::string& selection_name, const Rectangle& rectangle);
	bool is_within(const Rectangle& rectangle) {
		const Coordinates& tmp_coordinates = rectangle.get_coordinates();
		return (tmp_coordinates.x >= 0 && (tmp_coordinates.x + rectangle.get_height()) < this->get_height()
			&& tmp_coordinates.y >=0 && (tmp_coordinates.y + rectangle.get_width()) < this->get_width());
	}

	bool selection_exists_as_active(const std::string& selection_name) const {
		return active_selections.find(selection_name) != active_selections.end();
	}
	bool selection_exists_as_inactive(const std::string& selection_name) const {
		return inactive_selections.find(selection_name) != inactive_selections.end();
	}
	bool selection_exists(const std::string& selection_name) const {
		return selection_exists_as_active(selection_name) || selection_exists_as_inactive(selection_name);
	}

	void remove_selection(const std::string& selection_name);
	void set_selection_status(const std::string& selection_name, bool status);

	// Operations --------------------------------------------------------------
	void apply(const std::shared_ptr<Operation>& operation);

	// Other -----------------------------------------------------------------
	Layer& operator[](int k);	// returns layer reference
	const Layer& operator[](int k) const;
	std::string get_name() const {
		return name;
	}
	void set_name(const std::string& _name) {
		this->name = _name;
	}
	unsigned long get_width() const{
		return project.width;
	}
	unsigned long get_height() const {
		return project.height;
	}
	const Layer& get_project() const {
		return project;
	}
	bool get_saved_status() const {
		return this->saved_changes;
	}

private:
	std::string name;
	bool saved_changes;

	std::vector<Layer> layers;
	Layer project;

	Selection_Map active_selections;
	Selection_Map inactive_selections;

};
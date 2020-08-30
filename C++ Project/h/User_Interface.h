#pragma once
#include <memory>
#include "Composite_Operations_Map.h"

class Image;

class User_Interface {
private:
	std::shared_ptr<Image> image;
	Composite_Operations_Map composite_operations;

	// Menus -----------------------
	void start_menu()  const;
	void main_menu() const;

	void layers_config_menu() const;
	void selections_config_menu() const;
	void export_menu() const;
	void operations_menu() const;
	void composite_operations_menu() const;

	void composite_operations_config();


	// Interfaces ------------------
	void start_interface();
	void main_interface();

	void selections_config();
	void layers_config();
	void export_config();
	void operations_config();
public:

	void begin() {
		start_interface();
		main_interface();
	}
	
};


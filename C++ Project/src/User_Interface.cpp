#include "User_Interface.h"

#include "image.h"
#include "Operations_Map.h"

#include <iostream>
#include <sstream>
#include <string>
#include <exception>
#include <regex>

#include <algorithm>
#include <iterator>

using std::cout;
using std::cin;
using std::cerr;
using std::endl;


// Menus ------------------------------------------------------------------------------

void User_Interface::start_menu() const {
	cout << " ________________________________________________________" << endl;
	cout << "|                  PHOTOSHOP FOR POOR                    |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| New image ......................................... 1  |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| Quit .............................................. 0  |" << endl;
	cout << "|________________________________________________________|" << endl;

	cout << endl << "Choose any option: ";
}
void User_Interface::main_menu() const {
	cout << " ________________________________________________________" << endl;
	cout << "|                       Main menu                        |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| Image details ..................................... 1  |" << endl;
	cout << "| Load image (add layer) from file .................. 2  |" << endl;
	cout << "| Edit image ........................................ 3  |" << endl;
	cout << "| Composite operations configuration ...............  4  |" << endl;
	cout << "| Layers configuration .............................. 5  |" << endl;
	cout << "| Selections configuration .......................... 6  |" << endl;
	cout << "| Rename image ...................................... 7  |" << endl;
	cout << "| Export image ...................................... 8  |" << endl;
	cout << "| New image ......................................... 9  |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| Quit .............................................. 0  |" << endl;
	cout << "|________________________________________________________|" << endl;

	cout << endl << "Choose any option: ";
}
void User_Interface::layers_config_menu() const {
	cout << " ________________________________________________________" << endl;
	cout << "|                  Layer Configuration                   |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| Layer details ..................................... 1  |" << endl;
	cout << "| Add empty layer ................................... 2  |" << endl;
	cout << "| Move layer depth .................................. 3  |" << endl;
	cout << "| Move layer position within image................... 4  |" << endl;
	cout << "| Set opacity ....................................... 5  |" << endl;
	cout << "| Set active status ................................. 6  |" << endl;
	cout << "| Reset active status ............................... 7  |" << endl;
	cout << "| Set visible status ................................ 8  |" << endl;
	cout << "| Reset visible status .............................. 9  |" << endl;
	cout << "| Remove layer ..................................... 10  |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| Back .............................................. 0  |" << endl;
	cout << "|________________________________________________________|" << endl;

	cout << endl << "Choose any option: ";
}
void User_Interface::selections_config_menu() const {
	cout << " ________________________________________________________" << endl;
	cout << "|                 Selection Configuration                |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| Selection details ................................. 1  |" << endl;
	cout << "| Add selection ..................................... 2  |" << endl;
	cout << "| Remove selection .................................. 3  |" << endl;
	cout << "| Set active status ................................. 4  |" << endl;
	cout << "| Reset active status ............................... 5  |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| Back .............................................. 0  |" << endl;
	cout << "|________________________________________________________|" << endl;

	cout << endl << "Choose any option: ";
}
void User_Interface::export_menu() const {
	cout << " ________________________________________________________" << endl;
	cout << "|                     Export menu                        |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| Save as .bmp  ..................................... 1  |" << endl;
	cout << "| Save as .pam  ..................................... 2  |" << endl;
	cout << "| Save as .xml  ..................................... 3  |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| Back  ............................................. 0  |" << endl;
	cout << "|________________________________________________________|" << endl;

	cout << endl << "Choose any option: ";

}
void User_Interface::operations_menu() const {
	cout << " _________________________________________________________________" << endl;
	cout << "|                     Table of operations                         |" << endl;
	cout << "|                                                                 |" << endl;
	cout << "|----+-----------------------+------------------------------------|" << endl;
	cout << "|  # | Operation             | Code                               |" << endl;
	cout << "|----+-----------------------+------------------------------------|" << endl;
	cout << "|  1 | Add                   | add [value]                        |" << endl;
	cout << "|  2 | Subtract              | sub [value]                        |" << endl;
	cout << "|  3 | Multiply              | mul [value]                        |" << endl;
	cout << "|  4 | Divide                | div [value]                        |" << endl;
	cout << "|  5 | Inverse Subtract      | invsub [value]                     |" << endl;
	cout << "|  6 | Inverse Divide        | invdiv [value]                     |" << endl;
	cout << "|  7 | Power                 | pow [value]                        |" << endl;
	cout << "|  8 | Maximum               | max [value]                        |" << endl;
	cout << "|  9 | Minimum               | min [value]                        |" << endl;
	cout << "| 10 | Logarithm (base 10)   | log                                |" << endl;
	cout << "| 11 | Modulus               | abs                                |" << endl;
	cout << "| 12 | Invert                | inv                                |" << endl;
	cout << "| 13 | Black & White         | bnw                                |" << endl;
	cout << "| 14 | Grayscale             | grs                                |" << endl;
	cout << "| 15 | Paint                 | paint [R value][G value][B value]  |" << endl;
	cout << "| 16 | Median                | med                                |" << endl;
	cout << "|----------------------------+------------------------------------|" << endl;
	cout << "|                                                                 |" << endl;
	cout << "| Back  ....................................................... 0 |" << endl;
	cout << "|_________________________________________________________________|" << endl;

}
void User_Interface::composite_operations_menu() const {
	cout << " ________________________________________________________" << endl;
	cout << "|           Composite Operations Configuration           |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| Composite operations overview ..................... 1  |" << endl;
	cout << "| Create new composite operation .................... 2  |" << endl;
	cout << "| Apply composite operation ......................... 3  |" << endl;
	cout << "| Import composite operation from file .............. 4  |" << endl;
	cout << "| Export composite operation to file ................ 5  |" << endl;
	cout << "|                                                        |" << endl;
	cout << "| Back .............................................. 0  |" << endl;
	cout << "|________________________________________________________|" << endl;

	cout << endl << "Choose any option: ";
}

// Interfaces -------------------------------------------------------------------------

void User_Interface::start_interface() {
	int option;
	bool done = false;
	// Start menu ---------------------------------------------------------
	start_menu();
	while (!done) {
		cin >> option;
		switch (option) {
		case 1: {
			cout << "Set image name: ";
			std::string name;
			std::getline(cin >> std::ws, name);
			image = std::make_shared<Image>(name);
			done = true;
			break;
		}
		case 0: {
			cout << "Goodbye." << endl;
			exit(EXIT_SUCCESS);
		}
		default: {
			cout << "Invalid input. Choose from one of the given options: ";
			continue;
		}
		}

	}
}
void User_Interface::main_interface() {
	// Main menu ---------------------------------------------------------
	bool done = false;
	int option;

	main_menu();
	while (!done) {
		cin >> option;
		switch (option) {
		case 1: { image->details(std::cout); break; }
		case 2: {
			cout << "Enter file path: ";
			std::string file_path;
			cin >> file_path;	

			if (std::find(file_path.begin(), file_path.end(), '\\') == file_path.end())
				file_path = ".\\" + file_path;

			try {
				cout << "Loading image from file... " << endl;
				image->load(file_path);
				cout << "Done." << endl;

			}
			catch (const std::exception& e) {
				cerr << e.what() << endl;
			}

			break;
		}
		case 3: { operations_config(); break; }
		case 4: { composite_operations_config();  break; }
		case 5: { layers_config(); break; } 			// Layers configuration
		case 6: { selections_config(); break; }			// Selections configuration
		case 7: {
			cout << endl << "Current image name: " << image->get_name() << endl;
			cout << "Set new name: ";
			std::string new_name;
			std::getline(cin >> std::ws, new_name);
			image->set_name(new_name);
			cout << "Done." << endl;
			break;
		}
		case 8: { export_config(); break; }				// Exporting configuration
		case 9: { 
			if (image->get_saved_status() == false) {
				cout << "Save current image (0/1)? ";
				int option; cin >> option;
				if (option) {
					export_config();
					if (image->get_saved_status() == false)
						break;
				}

			}

			cout << "Set name of the new image: ";
			std::string name;
			std::getline(cin >> std::ws, name);

			image = std::make_shared<Image>(name); 
			
			break;

		}
		case 0: {
			if (image->get_saved_status() == false) {
				cout << "Save before leaving (0/1)? "; 
				int option; cin >> option;
				if (option) {
					export_config();
					if (image->get_saved_status() == false)
						break;
				}
			
			}
			cout << "Goodbye." << endl;
			done = true;
			continue;
		}
		default: { 
			cout << "Invalid input. Choose from one of the given options: ";
			continue;
		}

		}
		cout << endl;
		system("pause");
		main_menu();
	}

}
void User_Interface::selections_config() {
	bool done = false;
	int option;
	selections_config_menu();
	while (!done) {
		cin >> option;
		switch (option) {
		case 1: {
			image->selection_details(cout);
			break;
		}
		case 2: {
			cout << "Enter selection name: ";
			std::string selection_name; cin >> selection_name;
			cout << endl;

			Coordinates coordinates;
			unsigned long width;
			unsigned long height;

			while (true) {
				cout << "Enter parameters of the new rectangle within selection:" << endl;
				cout << "coordinates of the upper left corner: "; cin >> coordinates;
				cout << "width: "; cin >> width;
				cout << "height: "; cin >> height;
				cout << endl;

				try {
					cout << "Adding rectangle to selection \"" << selection_name << "\"..." << endl;
					image->add_rectangle(selection_name, Rectangle(coordinates, width, height));
					cout << "Rectangle added to selection. " << endl;
				}
				catch (const std::exception& e) {
					cerr << e.what() << endl;
				}

				cout << endl << "Add another rectangle (0/1)? ";
				int option; cin >> option;
				if (!option) break;

			}
			break;

		}
		case 3: {
			while (true) {
				cout << "Enter selection name: ";
				std::string selection_name; cin >> selection_name;
				try {
					cout << "Removing selection " << selection_name << "..." << endl;
					image->remove_selection(selection_name);
					cout << "Selection " << selection_name << "has been successfully removed." << endl;
					break;
				}
				catch (const std::exception& e) {
					cerr << e.what() << endl;
					cout << endl << "Try again (0/1)? ";
					int option;	cin >> option;
					if (!option) break;
				}
			}
			break;
		}
		case 4: {
			while (true) {
				cout << "Enter selection name: ";
				std::string selection_name; cin >> selection_name;
				try {
					cout << "Setting active status for selection \"" << selection_name << "\" ..." << endl;
					image->set_selection_status(selection_name, true);
					cout << "Status set to active." << endl;
					break;
				}
				catch (const std::exception& e) {
					cerr << e.what() << endl;
					cout << endl << "Try again (0/1)? ";
					int option; cin >> option;
					if (!option) break;
				}
			}
			break;
		}
		case 5: {
			while (true) {
				cout << "Enter selection name: ";
				std::string selection_name; cin >> selection_name;
				try {
					cout << "Setting inactive status for selection \"" << selection_name << "\" ..." << endl;
					image->set_selection_status(selection_name, false);
					cout << "Status set to inactive." << endl;
					break;
				}
				catch (const std::exception& e) {
					cerr << e.what() << endl;
					cout << endl << "Try again 0/1? ";
					int option;	cin >> option;
					if (!option) break;
				}
			}
			break;
		}
		case 0: {
			done = true;
			continue;
		}
		default: {
			cout << "Invalid input. Choose from one of the given options: ";
			continue;
		}
		}

		cout << endl;
		system("pause");
		selections_config_menu();
	}

}
void User_Interface::layers_config() {
	int option;
	layers_config_menu();
	bool done = false;
	while (!done) {
		cin >> option;
		switch (option) {
		case 0: { done = true; continue; }
		case 1: { image->layer_details(cout); break; }
		case 2: {											// Empty layer
			cout << "Enter layer dimensions:" << endl;
			int m, n;
			cout << "Width: "; cin >> m;
			cout << "Height: "; cin >> n;
			if (m <= 0 || n <= 0)
				cout << "Invalid layer dimensions." << endl;
			else {
				image->add_layer(Layer(EMPTY_LAYER, m, n));
				cout << "Done." << endl;
			}

			break;
		}
		default: {
			if (option >= 3 && option <= 10) {
				if (image->layers_num() == 0) {
					cout << "No existing layers." << endl;
					break;
				}
				std::vector<int> pending;

				cout << "Select layers on which the operation will be applied to: ";
				int x;
				cin.get();
				while ((cin.peek() != '\n') && (cin >> x)) {
					if (x >= 0 && x < (int)image->layers_num())
						pending.push_back(x);
					else {
						cout << endl << "Select one of the existing layers (" << 0 << "-" << image->layers_num() - 1 << "):";
						cin.get();
					}
				}
				if (option == 10) {	//removing layers requires sorted pending requests
					sort(pending.begin(), pending.end(), std::greater<int>());
					unique(pending.begin(), pending.end());
				}


				for (auto i : pending) {
					switch (option) {

					case 3: {	// Move layer depth
						cout << "Enter the new depth of layer #" << i << ": ";
						int depth;
						cin >> depth;
						try {
							cout << "Moving layer depth..." << endl;
							image->move_layer_depth(i, depth);
							cout << "Done." << endl;

						}
						catch (const std::exception& e) {
							cerr << e.what() << endl;
							continue;
						}
						break;

					}
							// Move layer position
					case 4: {
						cout << "New position of layer #" << i << " (enter the coordinates of the upper left corner): " << endl;
						int x, y;
						cout << "Width offset  = "; cin >> y;
						cout << "Height offset = "; cin >> x;

						cout << "Moving layer to position..." << endl;
						if (x < 0 || x + (*image)[i].get_height() > image->get_height() || y < 0 || y + (*image)[i].get_width() > image->get_width()) {
							cout << "New position out of bounds. Move by the edge [0] /  Cut layer to fit [1]: ";
							int cut; cin >> cut;

							if (cut)
								image->move_layer_and_cut(i, { x, y });
							else
								image->move_layer_position(i, { x, y });


						}
						else
							(*image)[i].set_position({ x,y });

						cout << "Done." << endl;


						break;
					}
							// Set opacity
					case 5: {
						cout << "Set opacity for layer #" << i << ": ";
						int opacity; cin >> opacity;
						cout << "Setting opacity..." << endl;
						(*image)[i].set_opacity(opacity);
						cout << "Done." << endl;
						break;
					}
							// Status settings
					case 6: {
						(*image)[i].set_active(true);
						cout << "Layer #" << i << " set as active." << endl;
						break;
					}
					case 7: {
						(*image)[i].set_active(false);
						cout << "Layer #" << i << " set as inactive." << endl;
						break;
					}
					case 8: {
						(*image)[i].set_visible(true);
						cout << "Layer #" << i << " set as visible." << endl;
						break;
					}
					case 9: {
						(*image)[i].set_visible(false);
						cout << "Layer #" << i << " set as invisible." << endl;
						break;
					}
							// Remove layer settings

					case 10: {
						cout << "Removing layer #" << i << "..." << endl;
						image->remove_layer(i);
						cout << "Layer #" << i << " removed." << endl;
						break;
					}

					}
				}
				pending.clear();

			}
			else {
				cout << "Invalid input. Choose from one of the given options: ";
				continue;
			}
			break;

		}
		}

		cout << endl;
		system("pause");
		layers_config_menu();

	}
}
void User_Interface::export_config() {
	std::string full_name = image->get_name();

	int option = 0;
	export_menu();
	while (true) {
		cin >> option;
		switch (option) {
		case 1: full_name += ".bmp"; break;
		case 2: full_name += ".pam"; break;
		case 3: full_name += ".xml"; break;
		case 0: return;
		default: cout << "Invalid input. Choose from one of the given options: "; continue;
		}
		break;
	}
	cout << endl;
	cout << "Enter file location (directory): ";
	std::string path; cin >> path;
	path += "\\" + full_name;

	try {
		cout << endl << "Saving as \"" << path <<  "\" ..." << endl;
		image->save(path);
		cout << "Done." << endl;
	}
	catch (const std::exception& e) {
		cerr << e.what() << endl;
	}

}
void User_Interface::operations_config() {
	operations_menu();
	std::string code;

	while (true) {
		cout << endl << "Enter operation code (or 0 to quit the operation menu): ";
		std::getchar();
		std::getline(cin, code);

		if (!std::regex_match(code, std::regex("^[ ]*0([ ]|$)"))) {
			try {
				cout << "Applying operation..." << endl;
				image->apply(Operation_Map::read_operation_code(code));
				cout << "Done." << endl;

			}
			catch (const std::exception& e) {
				cout << e.what() << endl;
			}
			cout << "Apply another operation (0/1)? ";
			int option; cin >> option; if (!option) break;
		}
		else
			break;
	}
}
void User_Interface::composite_operations_config() {

	int option;
	bool done = false;
	composite_operations_menu();
	while (!done) {
		cin >> option;
		switch (option) {
			// Details
		case 1: {
			cout << endl << "Composite operations (" << composite_operations.size() << ")" << endl;
			this->composite_operations.details(cout);
			break;
		}
				// Create composite operations
		case 2: {
			std::string op_name;
			cout << "Set name of new composite operation: ";
			cin >> op_name;

			cout << endl << "Composite operations (" << composite_operations.size() << ")" << endl << endl;
			this->composite_operations.details(cout);

			operations_menu();
			cout << endl << "Create composite operation by combining other operations." << endl << endl;

			if (!composite_operations[op_name].use_count())
				composite_operations[op_name] = std::make_shared<Composite_Operation>(op_name);

			std::string code;
			while (true) {
				cout << endl << "Enter operation code: ";
				std::getchar();
				std::getline(cin, code);

				if (std::regex_match(code, std::regex("^[ ]*0([ ]|$)")))
					break;
				if (code == op_name)
					cout << "Composite operation cannot contain itself." << endl;
				else
					try {
						cout << "Adding as a part of a composite operation..." << endl;

						if (composite_operations.find(code) != composite_operations.end())
							for (auto& operation : *composite_operations[code])
								composite_operations[op_name]->add_operation(operation);
						else
							composite_operations[op_name]->add_operation(Operation_Map::read_operation_code(code));

						cout << "Done." << endl << endl;

					}
					catch (const std::exception& e) {
						cerr << e.what() << endl << endl;
					}

				cout << "Add another operation (0/1)? ";
				int option; cin >> option; if (!option) break;
				
			}

			if (!composite_operations[op_name]->size())
				composite_operations.erase(op_name);

			break;
		}
		case 3: {
			this->composite_operations.details(cout);

			std::string op_name;
			cout << "Enter the name of composite operation: ";
			cin >> op_name;

			if (composite_operations.find(op_name) != composite_operations.end()) {
				cout << "Applying operation..." << endl;
				image->apply(composite_operations[op_name]);
				cout << "Done." << endl;
			}
			else
				cout << "Operation name not recognized.";

			break;

		}
		// Import
		case 4: {
			cout << "Enter file path: ";
			std::string path; cin >> path;
			std::shared_ptr<Composite_Operation> comp_op = std::make_shared<Composite_Operation>();

			try {
				cout << "Loading composite operation..." << endl;
				comp_op->import_from_file(path);
				this->composite_operations[comp_op->get_name()] = comp_op;
				cout << "Done." << endl;
			}
			catch (const std::exception& e) {
				cerr << e.what() << endl;
			}

			break;

		}


				// Export
		case 5: {
			std::string op_name;
			cout << "Enter the name of composite operation: ";
			cin >> op_name;

			if (composite_operations.find(op_name) != composite_operations.end()) {
				cout << endl << "Enter file location (directory): ";
				std::string path; cin >> path; path += "\\";

				cout << endl << "Saving..." << endl;
				try {
					composite_operations[op_name]->export_to_file(path);
				}
				catch (std::exception& e) {
					cerr << e.what() << endl;
				}
				cout << endl << "Composite function saved as " << path << op_name << ".fun" << endl;

			}
			else
				cout << "Operation name not recognized.";


			break;

		}

		case 0: {
			done = true;
			continue;
		}
		default: {
			cout << "Invalid input. Choose from one of the given options: ";
			continue;
		}

		}

		cout << endl;
		system("pause");
		composite_operations_menu();


	}



}

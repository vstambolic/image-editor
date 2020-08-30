#include "Image.h"
#include "Composite_Operation.h"
#include "User_Interface.h"

#include <iostream>
#include <memory>

#include <list>
#include <algorithm>
int main(int argc, char* argv[]) {

	if (argc == 3) {
		Image image;
		Composite_Operation comp_op;

		//std::cout << "Processing... " << std::endl;
		try {
			image.load(argv[1]);
			comp_op.import_from_file(argv[2]);

			image.apply(std::make_shared<Composite_Operation>(comp_op));

			image.save(argv[1]);

		}
		catch (const std::exception& e) {
			//std::cerr << e.what() << std::endl;
		}
		//std::cout << "Done!";


	}
	else 
		User_Interface().begin();

	return 0;
}


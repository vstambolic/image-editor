#include "Operation.h"

D_Pixel_Matrix & Operation::wrapper(const Selection_Map & selections, D_Pixel_Matrix & matrix, const Coordinates & coordinates, std::function<void(D_Pixel&)>&& func) {
	const int m = matrix.size();
	const int n = matrix[0].size();

	for (int i = 0; i < m; ++i)
		for (int j = 0; j < n; ++j)
			if (selections.end() != std::find_if(selections.begin(), selections.end(), [i, j, &coordinates](const std::pair<std::string, Selection>& p) { return p.second.is_selected({ i + coordinates.x, j + coordinates.y }); }))
				func(matrix[i][j]);  // if pixel {i,j} is selected  

	return matrix;
}

D_Pixel_Matrix & Operation::wrapper(D_Pixel_Matrix & matrix, std::function<void(D_Pixel&)>&& func) {
	for (auto& vec : matrix)
		for (auto& pix : vec)
			func(pix);
	return matrix;
}

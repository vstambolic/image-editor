#pragma once
#include <cstdint>  // uintX_t
#include <array>
#include <algorithm>
#include <limits>	// CHAR_BIT

typedef uint8_t Byte;									
typedef uint16_t Word;
typedef uint32_t Dword;

template <typename T>
T swap_bytes(const T& value) {							// alternative : _byte_swap from <cstdlib>
	union {												// alternative : bitwise operations (<bitset>)
		T val;
		std::array<Byte, sizeof(T)> bytes;
	} src, dst;

	src.val = value;
	std::reverse_copy(src.bytes.begin(), src.bytes.end(), dst.bytes.begin());
	return dst.val;
}

template<class T>
T ror(T x, unsigned moves) {
	return (x >> moves) | (x << (sizeof(T) * CHAR_BIT - moves));
}

template <typename T>
T rol(T x, unsigned moves) {
	return (x << moves) | (x >> (sizeof(T) * CHAR_BIT - moves));
}
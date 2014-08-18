LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# This is the target being built.
LOCAL_MODULE:= bspatch


# All of the source files that we will compile.
# 具体到底需要哪些c代码，没有仔细研究过
LOCAL_SRC_FILES:= combineDemo.c \
				  bzlib.c \
  				  blocksort.c \
  				  compress.c \
  				  crctable.c \
  				  decompress.c \
  				  huffman.c \
  				  randtable.c \
  				  bzip2.c \
  				    	
# No static libraries.
LOCAL_STATIC_LIBRARIES := \
     libbz


# Also need the JNI headers.
LOCAL_C_INCLUDES += \
    $(JNI_H_INCLUDE) external/bzip2

# No special compiler flags.
LOCAL_CFLAGS += 

include $(BUILD_SHARED_LIBRARY)
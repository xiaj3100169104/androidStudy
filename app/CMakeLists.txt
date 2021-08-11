
cmake_minimum_required(VERSION 3.4.1)

add_library( jni_test
             SHARED
             ${CMAKE_SOURCE_DIR}/src/main/cpp/src/test/test.cpp
             ${CMAKE_SOURCE_DIR}/src/main/cpp/src/test/logUtil.cpp )

include_directories(
    ${CMAKE_SOURCE_DIR}/src/main/cpp/include
)

find_library( log-lib
              log )

target_link_libraries( jni_test
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib})

add_library( yuv420p_to_rgba
        SHARED
        ${CMAKE_SOURCE_DIR}/src/main/cpp/src/yuv/yuv420p_to_rgba.cpp
        ${CMAKE_SOURCE_DIR}/src/main/cpp/src/yuv/ImageUtil.cpp )

find_library( log-lib
        log )

target_link_libraries( yuv420p_to_rgba
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
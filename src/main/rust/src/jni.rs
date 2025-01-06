use jni::{
    objects::{JClass, JDoubleArray, ReleaseMode},
    sys::{jdouble, jstring},
    JNIEnv,
};

// In this file we export the functions for the JNI interface

#[no_mangle]
pub extern "C" fn Java_golf_tweede_JniInterface_doubleToStringRust(
    env: JNIEnv,
    _class: JClass,
    value: jdouble,
) -> jstring {
    env.new_string(value.to_string()).unwrap().into_raw()
}

#[no_mangle]
pub extern "C" fn Java_golf_tweede_JniInterface_doubleToStringRyu(
    env: JNIEnv,
    _class: JClass,
    value: jdouble,
) -> jstring {
    let mut buffer = ryu::Buffer::new();
    env.new_string(buffer.format(value)).unwrap().into_raw()
}

#[no_mangle]
pub extern "C" fn Java_golf_tweede_JniInterface_doubleArrayToStringRyu(
    mut env: JNIEnv,
    _class: JClass,
    array: JDoubleArray,
) -> jstring {
    let mut buffer = ryu::Buffer::new();
    let len: usize = env.get_array_length(&array).unwrap().try_into().unwrap();
    let mut output = String::with_capacity(10 * len);

    {
        // limit scope of elements to prevent keeping env borrowed as mutable
        let elements = unsafe {
            env.get_array_elements_critical(&array, ReleaseMode::NoCopyBack)
                .unwrap()
        };

        for v in elements.iter() {
            output.push_str(buffer.format(*v));
            output.push(' ');
        }
    }

    env.new_string(output).unwrap().into_raw()
}

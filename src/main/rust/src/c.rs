use std::ffi::{c_char, c_double, CString};

// In this file we export the functions for a generic C interface, used by JNR-FFI and Project Panama

#[no_mangle]
pub extern "C" fn doubleToStringRust(value: c_double) -> *mut c_char {
    CString::new(value.to_string()).unwrap().into_raw()
}

#[no_mangle]
pub extern "C" fn doubleToStringRyu(value: c_double) -> *mut c_char {
    let mut buffer = ryu::Buffer::new();
    CString::new(buffer.format(value)).unwrap().into_raw()
}

#[no_mangle]
pub unsafe extern "C" fn doubleArrayToStringRyu(array: *const c_double, len: usize) -> *mut c_char {
    let mut buffer = ryu::Buffer::new();
    let mut output = String::with_capacity(10 * len);
    let slice = std::slice::from_raw_parts(array, len);

    for v in slice.iter() {
        output.push_str(buffer.format(*v));
        output.push(' ');
    }

    CString::new(output).unwrap().into_raw()
}

#[no_mangle]
pub unsafe extern "C" fn freeString(string: *mut c_char) {
    let _ = CString::from_raw(string);
}

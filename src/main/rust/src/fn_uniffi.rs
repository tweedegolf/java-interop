#[uniffi::export]
fn double_to_string_rust(value: f64) -> String {
    value.to_string()
}

#[uniffi::export]
fn double_to_string_ryu(value: f64) -> String {
    let mut buffer = ryu::Buffer::new();
    buffer.format(value).to_string()
}

#[uniffi::export]
fn double_array_to_string_ryu(array: &[f64]) -> String {
    let mut buffer = ryu::Buffer::new();
    let mut output = String::with_capacity(10 * array.len());

    for v in array.iter() {
        output.push_str(buffer.format(*v));
        output.push(' ');
    }

    output
}

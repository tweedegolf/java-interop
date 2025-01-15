fn main() {
    // Create C headers with cbindgen
    let crate_dir = std::env::var("CARGO_MANIFEST_DIR").unwrap();
    cbindgen::Builder::new()
        .with_crate(crate_dir.clone())
        .with_language(cbindgen::Language::C)
        .generate()
        .unwrap()
        .write_to_file("bindings/java_interop.h");

    // Create Java interface with JExtract
    let java_project_dir = std::path::Path::new(&crate_dir).ancestors().nth(3).unwrap();
    std::process::Command::new("jextract")
        .current_dir(java_project_dir)
        .arg("--include-dir")
        .arg("src/main/rust/bindings/")
        .arg("--output")
        .arg("src/main/java")
        .arg("--target-package")
        .arg("golf.tweede.gen")
        .arg("--library")
        .arg(":src/main/rust/target/release/libjava_interop.so")
        .arg("src/main/rust/bindings/java_interop.h")
        .spawn()
        .unwrap();
}

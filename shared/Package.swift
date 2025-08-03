// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "Shared",
    platforms: [
        .iOS(.v17),
    ],
    products: [
        .library(
            name: "Shared",
            targets: ["Shared"]
        ),
    ],
    dependencies: [
        // Add any Swift dependencies here if needed
    ],
    targets: [
        .binaryTarget(
            name: "Shared",
            path: "./build/XCFrameworks/Shared.xcframework"
        )
    ]
)
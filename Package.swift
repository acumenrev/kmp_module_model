// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "KMPShared",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "KMPShared",
            targets: ["KMPShared"]
        ),
    ],
    dependencies: [
        // Add any Swift dependencies here if needed
    ],
    targets: [
        .binaryTarget(
            name: "KMPShared",
            path: "./shared/build/XCFrameworks/release/KMPShared.xcframework"
        )
    ]
)
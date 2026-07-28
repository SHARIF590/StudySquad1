sed -i '/\/\/ Helper to draw simple text/d' app/src/main/java/com/example/ui/screens/AnalyticsScreen.kt
sed -i '/drawContext.canvas.nativeCanvas.drawText/d' app/src/main/java/com/example/ui/screens/AnalyticsScreen.kt
sed -i '/^}/d' app/src/main/java/com/example/ui/screens/AnalyticsScreen.kt # careful with this

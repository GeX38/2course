private fun adjustCardSizes() {
    val totalCardArea = calculateTotalCardArea()
    val screenWidth = resources.displayMetrics.widthPixels

    val maxColumnCount = gridLayout.columnCount
    val cardWidth = (screenWidth - 10 * (maxColumnCount + 1)) / maxColumnCount
    val cardHeight = (cardWidth * 0.6).toInt()

    for (i in 0 until gridLayout.childCount) {
        val child = gridLayout.getChildAt(i)
        if (child is TextView) {
            val layoutParams = child.layoutParams as? GridLayout.LayoutParams
            layoutParams?.width = cardWidth
            layoutParams?.height = cardHeight
            child.layoutParams = layoutParams
        }
    }
}

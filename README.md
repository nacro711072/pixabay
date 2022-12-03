# pixabayApp

透過Pixabay API, 進行圖片搜尋的 APP

## 說明

1. 採用 MVVM + repository
2. 使用Koin DI
3. UI: Compose UI 建構。
4. API request 則為 OkHttp + Retrofit
5. 圖片處理使用 `Coil`, 一個專門為kotlin 處理圖片的 package, 用法跟`glide` 極為相似
6. viewModel 使用StateFlow 取代 LiveData.

## 功能
- 點擊 上方 Search Bar 進行搜尋
- 使用 firebase remote config, 維運者可切換 預設 layout 樣式 (Grid / List).
- 使用者可隨意切換 layout 樣式 (Grid / List).
- 儲存使用者搜尋紀錄, 並在下次搜尋時跳出近似選項提示
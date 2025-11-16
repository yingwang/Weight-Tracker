# Weight Tracker - Android应用

一个功能完整、界面清爽的Android体重追踪应用，采用现代化设计理念和最新技术栈开发。

## ✨ 主要功能

### 📊 核心功能
- **体重记录管理**
  - 添加体重记录，支持备注
  - 查看历史记录列表
  - 删除不需要的记录
  - 自动计算并显示BMI指数

- **数据可视化**
  - 周视图：查看最近7天的体重变化趋势
  - 月视图：查看最近30天的体重变化趋势
  - 年视图：查看最近一年的体重变化趋势
  - 实时统计：最小值、最大值、平均值、变化量

- **目标管理**
  - 设置目标体重和达成日期
  - 实时显示目标完成进度
  - 剩余天数和剩余体重提醒
  - 达成目标时的庆祝提示

- **健康数据集成**
  - 集成Google Health Connect
  - 获取每日步数数据
  - 支持运动数据追踪
  - 一键授权健康数据权限

- **个人资料管理**
  - 设置身高、年龄、性别
  - 自动计算BMI和健康状态
  - 支持公制(kg)和英制(lbs)单位切换
  - 个人数据安全存储

## 🎨 设计特色

### Material Design 3
- 采用最新的Material Design 3设计规范
- 支持动态颜色主题（Android 12+）
- 深色模式自适应
- 流畅的动画和过渡效果

### 清爽科技感UI
- 现代化卡片设计
- 柔和的圆角和阴影
- 清晰的视觉层级
- 舒适的配色方案
  - 主色：科技蓝 (#6B9BFF)
  - 辅色：健康绿 (#4CAF50)
  - 背景：浅灰白 (#F5F7FA)

## 🏗️ 技术栈

### 架构和框架
- **开发语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构模式**: MVVM (Model-View-ViewModel)
- **最小SDK**: Android 8.0 (API 26)
- **目标SDK**: Android 14 (API 34)

### 核心库
- **Jetpack Compose**: 现代化声明式UI
- **Room Database**: 本地数据持久化
- **Kotlin Coroutines**: 异步编程
- **Flow**: 响应式数据流
- **ViewModel & LiveData**: 生命周期感知的数据管理
- **Navigation Compose**: 导航管理

### 数据可视化
- **Vico Charts**: 现代化的Compose图表库
- 支持Material 3主题
- 流畅的动画效果
- 高度可定制

### 健康数据
- **Health Connect**: Google官方健康数据平台
- 支持步数、体重、运动数据
- 隐私优先的数据访问

## 📱 界面预览

### 主要页面
1. **首页 (Home)**
   - 当前体重大卡片显示
   - BMI指数和健康分类
   - 目标进度条
   - 最近记录列表

2. **图表页 (Charts)**
   - 周/月/年视图切换
   - 体重趋势曲线
   - 统计数据卡片
   - 变化趋势分析

3. **目标页 (Goals)**
   - 目标进度环形显示
   - 起始/当前/目标体重对比
   - 剩余天数倒计时
   - 设置新目标

4. **个人资料 (Profile)**
   - 用户信息展示
   - 健康数据统计
   - Health Connect集成
   - 个人信息编辑

## 🚀 快速开始

### 环境要求
- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17 或更高版本
- Android SDK 34
- Gradle 8.2+

### 构建步骤

1. **克隆项目**
```bash
git clone https://github.com/yourusername/Weight-Tracker.git
cd Weight-Tracker
```

2. **打开项目**
- 使用Android Studio打开项目
- 等待Gradle同步完成

3. **运行应用**
- 连接Android设备或启动模拟器
- 点击运行按钮或执行：
```bash
./gradlew installDebug
```

### 配置Health Connect

1. 确保设备安装了Health Connect应用
2. 首次使用时，应用会请求必要的权限
3. 在Profile页面点击"Connect Health Data"授权

## 📦 项目结构

```
app/src/main/java/com/weighttracker/
├── data/                    # 数据层
│   ├── database/           # Room数据库
│   │   ├── WeightDatabase.kt
│   │   └── Converters.kt
│   ├── dao/                # 数据访问对象
│   │   ├── WeightEntryDao.kt
│   │   ├── GoalDao.kt
│   │   └── UserProfileDao.kt
│   ├── entity/             # 数据实体
│   │   ├── WeightEntry.kt
│   │   ├── Goal.kt
│   │   └── UserProfile.kt
│   └── repository/         # 数据仓库
│       └── WeightRepository.kt
├── ui/                      # UI层
│   ├── screens/            # 屏幕组件
│   │   ├── HomeScreen.kt
│   │   ├── ChartsScreen.kt
│   │   ├── GoalsScreen.kt
│   │   ├── ProfileScreen.kt
│   │   └── AddWeightScreen.kt
│   ├── components/         # 可复用组件
│   ├── navigation/         # 导航配置
│   │   └── NavGraph.kt
│   └── theme/              # 主题配置
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── viewmodel/              # 视图模型
│   ├── WeightViewModel.kt
│   └── WeightViewModelFactory.kt
├── utils/                  # 工具类
│   ├── BMICalculator.kt
│   ├── DateUtils.kt
│   └── HealthConnectManager.kt
├── MainActivity.kt         # 主活动
└── WeightTrackerApp.kt    # Application类
```

## 🎯 数据模型

### WeightEntry (体重记录)
- `id`: 唯一标识
- `weight`: 体重（kg）
- `timestamp`: 记录时间
- `note`: 备注
- `bmi`: BMI指数

### Goal (目标)
- `id`: 唯一标识
- `targetWeight`: 目标体重
- `startWeight`: 起始体重
- `startDate`: 开始日期
- `targetDate`: 目标日期
- `isActive`: 是否激活

### UserProfile (用户资料)
- `id`: 唯一标识
- `height`: 身高（cm）
- `age`: 年龄
- `gender`: 性别
- `preferredUnit`: 偏好单位

## 🔒 隐私和安全

- 所有数据存储在设备本地
- 不收集或上传任何个人信息
- Health Connect数据仅在授权后访问
- 支持数据导出和备份

## 🛠️ 开发计划

### 已完成功能 ✅
- [x] 体重记录的增删查功能
- [x] Room数据库集成
- [x] 周/月/年视图图表
- [x] 目标设置和进度追踪
- [x] Health Connect集成
- [x] Material Design 3主题
- [x] BMI计算和分类

### 计划中功能 🚧
- [ ] 数据导出（CSV/Excel）
- [ ] 云端备份同步
- [ ] 提醒通知功能
- [ ] 体重预测算法
- [ ] 更多健康指标（体脂率、肌肉量等）
- [ ] 社交分享功能
- [ ] 多语言支持
- [ ] Widgets桌面小部件

## 📝 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。

## 🤝 贡献

欢迎提交Issue和Pull Request！

1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

## 📧 联系方式

如有问题或建议，请通过GitHub Issues联系。

## 🙏 致谢

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Vico Charts](https://github.com/patrykandpatrick/vico)
- [Health Connect](https://developer.android.com/health-and-fitness/guides/health-connect)

---

⭐ 如果这个项目对你有帮助，欢迎给个Star！

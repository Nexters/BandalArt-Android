package com.nexters.bandalart.ui

//@Composable
//fun BandalartApp() {
//    val snackbarHostState = remember { SnackbarHostState() }
//    val height = LocalConfiguration.current.screenHeightDp
//
//    Scaffold(
//        snackbarHost = {
//            SnackbarHost(
//                modifier = Modifier
//                    .padding(bottom = (height - 96).dp)
//                    .height(36.dp),
//                hostState = snackbarHostState,
//                snackbar = {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 40.dp),
//                        shape = RoundedCornerShape(50.dp),
//                        colors = CardDefaults.cardColors(containerColor = White),
//                        elevation = CardDefaults.cardElevation(8.dp),
//                    ) {
//                        Box(Modifier.fillMaxSize()) {
//                            Text(
//                                text = it.visuals.message,
//                                color = Gray700,
//                                fontSize = 12.sp,
//                                fontWeight = FontWeight.W600,
//                                modifier = Modifier.align(Alignment.Center),
//                                letterSpacing = -(0.24).sp,
//                            )
//                        }
//                    }
//                },
//            )
//        },
//    ) { innerPadding ->
//        BandalartNavHost(
//            modifier = Modifier.padding(innerPadding),
//            onShowSnackbar = { message ->
//                snackbarHostState.showSnackbar(
//                    message = message,
//                    duration = SnackbarDuration.Short,
//                ) == SnackbarResult.ActionPerformed
//            },
//        )
//    }
//}

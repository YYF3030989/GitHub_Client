package com.example.githubclient.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.githubclient.R
import com.example.githubclient.data.model.GithubUserDetail

@Composable
fun UserDetailHeader(detail: GithubUserDetail?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.detail_username) + detail?.login,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(text = stringResource(R.string.id) + detail?.id, style = MaterialTheme.typography.bodyMedium)
        detail?.name?.let {
            Text(text = stringResource(R.string.detail_name) + it, style = MaterialTheme.typography.bodyMedium)
        }
        detail?.bio?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, style = MaterialTheme.typography.bodySmall)
        }
    }
}
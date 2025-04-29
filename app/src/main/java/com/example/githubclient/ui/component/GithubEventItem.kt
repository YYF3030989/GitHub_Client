package com.example.githubclient.ui.component

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.githubclient.R
import com.example.githubclient.data.model.GithubEvent
import com.example.githubclient.ui.common.getIconForEvent

@Composable
fun GithubEventItem(event: GithubEvent) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = getIconForEvent(event.type),
            contentDescription = event.type,
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            verticalArrangement = Center,
            modifier = Modifier.weight(1f)
        ) {
            val eventType = if (event.type.endsWith(stringResource(R.string.event_suffix))) {
                event.type.removeSuffix(stringResource(R.string.event_suffix))
            } else {
                event.type
            }
            Text(text = eventType, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text(
                text = event.repo.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
package com.example.githubclient.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMerge
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.CommentBank
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.vector.ImageVector

fun getIconForEvent(eventType: String): ImageVector {
    return when (eventType) {
        "PushEvent" -> Icons.Default.CloudUpload
        "PullRequestEvent" -> Icons.AutoMirrored.Filled.CallMerge
        "IssuesEvent" -> Icons.Default.ErrorOutline
        "ForkEvent" -> Icons.AutoMirrored.Filled.CallSplit
        "WatchEvent" -> Icons.Default.Visibility
        "CreateEvent" -> Icons.Default.Add
        "DeleteEvent" -> Icons.Default.Delete
        "ReleaseEvent" -> Icons.Default.NewReleases
        "IssueCommentEvent" -> Icons.AutoMirrored.Filled.Comment
        "PullRequestReviewCommentEvent" -> Icons.Default.CommentBank
        else -> Icons.AutoMirrored.Filled.HelpOutline
    }
}
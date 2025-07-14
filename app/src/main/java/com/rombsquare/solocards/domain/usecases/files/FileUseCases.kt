package com.rombsquare.solocards.domain.usecases.files

data class FileUseCases(
    val getFiles: GetFiles,
    val addFile: AddFile,
    val getTags: GetTags,
    val getFilesByTag: GetFilesByTag,
    val clearTrash: ClearTrash,
    val restoreFile: RestoreFile,
    val getFileById: GetFileById,
    val updateFile: UpdateFile,
    val deleteFile: DeleteFile,
    val trashFile: TrashFile,
    val deleteAllFiles: DeleteAllFiles
)
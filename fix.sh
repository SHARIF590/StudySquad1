sed -i 's/ambientAudioName = "Cafe White Noise"/ambientAudioName = "Cafe White Noise"\n            )\n        )/g' app/src/main/java/com/example/data/repository/StudySquadRepository.kt
sed -i '461d' app/src/main/java/com/example/data/repository/StudySquadRepository.kt
echo "}" >> app/src/main/java/com/example/viewmodel/StudySquadViewModel.kt

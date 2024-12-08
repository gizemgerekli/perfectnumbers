using UnityEngine;
using TMPro;

public class PerfectNumberSpawner : MonoBehaviour
{
    public GameObject numberPrefab;
    public Transform[] spawnPoints;

    private void Start()
    {
        SpawnNumbers();
    }

    private void SpawnNumbers()
    {
        foreach (Transform spawnPoint in spawnPoints)
        {
            int number = GetRandomPerfectNumber();
            GameObject numberInstance = Instantiate(numberPrefab, spawnPoint.position, Quaternion.identity);
            numberInstance.GetComponent<TextMeshPro>().text = number.ToString();
        }
    }

    private int GetRandomPerfectNumber()
    {
        int[] perfectNumbers = { 6, 28, 496, 8128 };
        int randomIndex = Random.Range(0, perfectNumbers.Length);
        return perfectNumbers[randomIndex];
    }
}

using UnityEngine;
using TMPro;
using System.Collections.Generic;

public class GameManager : MonoBehaviour
{
    public GameObject numberPrefab;
    public TextMeshProUGUI hintText;
    public Transform[] spawnPoints;
    private List<int> perfectNumbers = new List<int> { 6, 28, 496, 8128 };
    private string playerName;
    private int hintCounter = 0;

    void Start()
    {
        playerName = PlayerPrefs.GetString("PlayerName");
        hintText.text = "Hoþ geldin " + playerName + "! Mükemmel sayýlarý bulmaya çalýþ.";
        PlaceNumbersInMaze();
    }

    void PlaceNumbersInMaze()
    {
        foreach (Transform spawnPoint in spawnPoints)
        {
            int randomIndex = Random.Range(0, perfectNumbers.Count);
            int number = perfectNumbers[randomIndex];
            GameObject numberObject = Instantiate(numberPrefab, spawnPoint.position, Quaternion.identity);
            numberObject.GetComponent<TextMeshPro>().text = number.ToString();
        }
    }

    public void OnPerfectNumberFound(int number)
    {
        if (perfectNumbers.Contains(number))
        {
            hintCounter++;
            hintText.text = "Ýpucu " + hintCounter + ": Çýkýþa biraz daha yaklaþtýn!";
        }
    }
}

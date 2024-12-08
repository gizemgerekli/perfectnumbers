using UnityEngine;
using TMPro;


public class PerfectNumber : MonoBehaviour
{
    private GameManager gameManager;
    private int number;

    void Start()
    {
        gameManager = FindObjectOfType<GameManager>();
        number = int.Parse(GetComponent<TextMeshPro>().text);
    }

    void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag("Player"))
        {
            gameManager.OnPerfectNumberFound(number);
        }
    }
}

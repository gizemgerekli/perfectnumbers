using UnityEngine;

public class MazeGenerator : MonoBehaviour
{
    public int width = 10;
    public int height = 10;
    public GameObject wallPrefab;
    private int[,] maze;

    void Start()
    {
        maze = GenerateMaze(width, height);
        DrawMaze(maze);
    }

    int[,] GenerateMaze(int width, int height)
    {
        int[,] maze = new int[width, height];

        // Initialize maze with walls
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                maze[x, y] = 1;
            }
        }

        // Create the maze using a simple algorithm
        for (int x = 1; x < width - 1; x += 2)
        {
            for (int y = 1; y < height - 1; y += 2)
            {
                maze[x, y] = 0;
                if (Random.Range(0, 2) == 0)
                {
                    maze[x + 1, y] = 0;
                }
                else
                {
                    maze[x, y + 1] = 0;
                }
            }
        }

        return maze;
    }

    void DrawMaze(int[,] maze)
    {
        for (int x = 0; x < maze.GetLength(0); x++)
        {
            for (int y = 0; y < maze.GetLength(1); y++)
            {
                if (maze[x, y] == 1)
                {
                    Instantiate(wallPrefab, new Vector3(x, 0, y), Quaternion.identity, transform);
                }
            }
        }
    }
}

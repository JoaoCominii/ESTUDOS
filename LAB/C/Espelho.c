#include <stdio.h>
#include <stdlib.h>

int main()
{
    int a, b;
    scanf("%i %i", &a, &b);
    for(int i = a; i <= b; i++)
    {
        printf("%i", i);
    }
    for(int i = b; i >= a; i--)
    {
        if(i > 99)
        {
            printf("%i", i % 100);
            int j = i / 10;
            printf("%i", j % 10);
            printf("%i", j / 10);
        }
        else if(i > 9)
        {
            printf("%i", i % 10);
            printf("%i", i / 10);
        }
        else printf("%i", i);
    }




    return 0;
}

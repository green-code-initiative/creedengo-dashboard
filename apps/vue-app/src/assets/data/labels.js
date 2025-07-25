export default {
    scores: [
        {
            score: 'A',
            labelBold: 'Your app is fully optimized, congratulations!',
            label: 'Don\'t forget to check it again if you update your app',
            labelLong: '100 % optimized, congrats!'
        },
        {
            score: 'B',
            labelBold: 'Your app is nearly optimized.',
            label: 'Well done! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.',
            labelLong: 'You have between 1 and 9 minor severities.'
        },
        {
            score: 'C',
            labelBold: 'Your app is not fully optimized.',
            label: 'Keep going! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.',
            labelLong: 'You have between 10 and 19 minor severities or you have 1 or many major severity.'
        },
        {
            score: 'D',
            labelBold: 'Many elements of your application can be optimized.',
            label: 'Don\'t worry! You can start by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.',
            labelLong: 'You have more than 20 minor severities or more than 10 major severities or 1 or many critical severities.'
        },
            {
            score: 'E',
            labelBold: 'Several elements of your application can be optimized.',
            label: 'Don\'t worry! You can start by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.',
            labelLong: 'You have 1 or more than 1 blocker severities.'
        }
    ]
}
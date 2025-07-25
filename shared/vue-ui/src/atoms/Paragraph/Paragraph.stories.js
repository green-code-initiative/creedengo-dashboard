export default {
    title: 'Design System/Atoms/Paragraph',
    component: HTMLParagraphElement,
    tags: ['autodocs'],
    argTypes: {
        score: {
            control: { type: 'text' },
            describe: 'Bloc de texte',
            defaultValue: 'Texte de paragraphe',
        }
    }
};

export const paragraph = {
    args: {
        value: 'Ceci est un paragraphe d’exemple. Il peut contenir du texte, des liens, et d’autres éléments HTML. Utilisez-le pour afficher des informations textuelles dans votre application.'
    }
}
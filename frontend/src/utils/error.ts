export type ErrorDisplayOptions = {
    title: string;
    message: string;
    duration?: number;
};

export function displayError(error: ErrorDisplayOptions) {
    console.error(error);
    alert(`${error.title}: ${error.message}`);
}

export const user = state => state.user

export const hasBoards = state => {
    return state.boards.length > 0
}

export const personalBoards = state => {
    return state.boards.filter(board => board.teamId === 0)
}

export const teamBoards = state => {

    // 새로운 배열 생성
    const teams = []

    state.teams.forEach(team => {
        teams.push({
            id: team.id,
            name: team.name,
            // 관련된 팀에 속하는 보드만 골라서 가져온다.
            boards: state.boards.filter(board => board.teamId === team.id)
        })
    })
    return teams
}